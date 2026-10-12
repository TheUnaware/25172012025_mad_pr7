package com.example.a25172012025_mad_pr7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a25172012025_mad_pr7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private val personList = ArrayList<Person>()
    private lateinit var adapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = DatabaseHelper(this)
        adapter = PersonAdapter(this, personList)
        binding.recyclerView.adapter = adapter

        getPersonDetailsFromSqliteDb()

        binding.fab.setOnClickListener {
            syncNetworkData()
        }
    }

    private fun syncNetworkData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    "https://api.json-generator.com/templates/5rDXHcbgpo93/data",
                    "d7wrtfqywyhu7y2bcbsz3cgjpbfisuhnmbibvgvf"
                )
                withContext(Dispatchers.Main) {
                    try {
                        if (data != null)
                            runOnUiThread { getPersonDetailsFromJson(data) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPersonDetailsFromSqliteDb() {
        val size = personList.size
        personList.clear()
        adapter.notifyItemRangeRemoved(0, size)
        try {
            personList.addAll(db.allPersons)
            adapter.notifyItemRangeInserted(0, personList.size)
        } catch (ee: JSONException) {
            ee.printStackTrace()
        }
    }

    private fun getPersonDetailsFromJson(sJson: String?) {
        val size = personList.size
        personList.clear()
        adapter.notifyItemRangeRemoved(0, size)
        try {
            val jsonArray = JSONArray(sJson)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray[i] as JSONObject
                val person = Person(jsonObject)
                personList.add(person)
                try {
                    if (db.getPerson(person.id) == null)
                        db.insertPerson(person)
                    else
                        db.updatePerson(person)
                } catch (ee: Exception) {
                    ee.printStackTrace()
                }
            }
            adapter.notifyItemRangeInserted(0, personList.size)
        } catch (ee: JSONException) {
            ee.printStackTrace()
        }
    }

    fun deletePerson(position: Int) {
        if (position in personList.indices) {
            val person = personList[position]
            db.deletePerson(person)
            personList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }
}