package com.example.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var linearAccelerationSensor: Sensor? = null
    private var threshold: Double = 3.5
    private var lastTimeToastShown: Long = 0
    private val debounce: Long = 3000

    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]
                val magnitude = sqrt((x * x + y * y + z * z).toDouble())

                if (magnitude > threshold && System.currentTimeMillis() - lastTimeToastShown >= debounce) {
                    lastTimeToastShown = System.currentTimeMillis()
                    Log.d("Movement", "Movement detected: $magnitude, threshold: $threshold")
                    Toast.makeText(this@MainActivity, "Movement Detected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        if (linearAccelerationSensor == null) {
            Toast.makeText(this, "No accelerometer sensor found on this device", Toast.LENGTH_LONG).show()
        } else {
            val moveSeekBar: SeekBar = findViewById(R.id.moveSeekBar)
            moveSeekBar.progress = 50
            moveSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    threshold = 0.05 + (6.95 * progress / 100)
                    Log.d("SeekBar", "Threshold set to: $threshold")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.d("SeekBar", "Started tracking touch")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.d("SeekBar", "Stopped tracking touch")
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        linearAccelerationSensor?.also {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}
