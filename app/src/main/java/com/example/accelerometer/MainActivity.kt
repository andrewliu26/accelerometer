package com.example.accelerometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private var seekBarValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.moveSeekBar)
        seekBarValue = seekBar.progress
    }
}