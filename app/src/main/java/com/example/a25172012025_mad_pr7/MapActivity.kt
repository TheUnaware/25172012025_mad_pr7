package com.example.a25172012025_mad_pr7

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a25172012025_mad_pr7.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private val TAG = "MapActivity"
    private lateinit var binding: ActivityMapBinding
    private var lat: Double = 0.0
    private var log: Double = 0.0
    private var title: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        val obj = intent.getSerializableExtra("Object") as? Person
        Log.i(TAG, "onCreate: Object:$obj")
        if (obj != null) {
            lat = obj.latitude
            log = obj.longitude
            title = obj.name

            binding.toolbarMap.title = title
            binding.textViewMapTitle.text = title
            binding.textViewMapAddress.text = obj.address
            binding.textViewMapCoords.text = "Latitude: $lat, Longitude: $log"

            binding.webViewMap.settings.javaScriptEnabled = true
            binding.webViewMap.webViewClient = WebViewClient()
            val mapUrl = "https://maps.google.com/maps?q=$lat,$log&z=10&output=embed"
            binding.webViewMap.loadUrl(mapUrl)
        }
    }
}
