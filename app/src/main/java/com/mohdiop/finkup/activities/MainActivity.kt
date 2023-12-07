package com.mohdiop.finkup.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.mohdiop.finkup.R

class MainActivity : AppCompatActivity() {

    private lateinit var add: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listeners()
    }

    private fun init() {
        add = findViewById(R.id.add)
    }

    private fun listeners() {
        add.setOnClickListener {
            startActivity(Intent(this, AddFink::class.java))
        }
    }
}