package com.example.android_ex

import android.hardware.SensorManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.android_ex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var mSensor:Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        /*supportFragmentManager.commit {
            replace(R.id.fragmentContainerView,DefaultFragment())
        }*/
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor



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

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when(event?.sensor?.type){
            Sensor.TYPE_ACCELEROMETER ->{
                var x = event.values[0]
                if (x>0){
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainerView, TrafficFragment())
                    }
                }else if (x<0) {
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainerView, WeatherFragment())
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
}