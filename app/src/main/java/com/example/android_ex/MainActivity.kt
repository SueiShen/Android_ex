package com.example.android_ex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.android_ex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        /*supportFragmentManager.commit {
            replace(R.id.fragmentContainerView,DefaultFragment())
        }*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, DefaultFragment())
            .commit()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btWeather.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainerView, WeatherFragment())
            }
        }
        binding.btTraffic.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainerView, TrafficFragment())
            }
        }
    }
}