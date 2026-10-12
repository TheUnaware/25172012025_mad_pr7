package com.example.a25172012025_mad_pr7

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a25172012025_mad_pr7.databinding.ListItemPersonBinding
import java.io.Serializable

class PersonAdapter(
    private val context: Context,
    private val array: ArrayList<Person>
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(val binding: ListItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding =
            ListItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val binding = holder.binding
        with(array[position]) {
            binding.textViewName.text = name
            binding.textViewPhone.text = phoneNo
            binding.textViewEmail.text = emailId
            binding.textViewAddress.text = address

            val obj = this as Serializable
            binding.buttonMap.setOnClickListener {
                Intent(this@PersonAdapter.context, MapActivity::class.java).apply {
                    putExtra("Object", obj)
                    this@PersonAdapter.context.startActivity(this)
                }
                //Toast.makeText(this@PersonAdapter.context, "Clicked on "+binding.textViewName, Toast.LENGTH_SHORT).show()
            }

            binding.buttonDelete.setOnClickListener {
                (context as MainActivity).deletePerson(holder.adapterPosition)
            }
        }
    }
}
