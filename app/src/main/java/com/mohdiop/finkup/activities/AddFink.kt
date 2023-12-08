package com.mohdiop.finkup.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.mohdiop.finkup.R
import com.mohdiop.finkup.database.Fink
import com.mohdiop.finkup.database.FinkDao
import com.mohdiop.finkup.database.FinkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFink : AppCompatActivity() {

    private lateinit var back: ImageButton
    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var add: Button

    private lateinit var finkDatabase: FinkDatabase
    private lateinit var finkDao: FinkDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fink)
        init()
        listeners()
    }

    private fun init() {
        back = findViewById(R.id.backToMainAdd)
        title = findViewById(R.id.titleAdd)
        content = findViewById(R.id.contentAdd)
        add = findViewById(R.id.addFink)
        finkDatabase = Room.databaseBuilder(this, FinkDatabase::class.java, "fink_database").build()
        finkDao = finkDatabase.finkDao()
    }

    private fun listeners() {
        back.setOnClickListener {
            this.finish()
        }
        add.setOnClickListener {
            if (title.text.isNotEmpty() and content.text.isNotEmpty()) {
                val finkToAdd = Fink(
                    finkTitle = title.text.toString(),
                    finkContent = content.text.toString(),
                    finkDate = System.currentTimeMillis()
                )
                CoroutineScope(Dispatchers.Main).launch {
                    finkDao.insertFink(finkToAdd)
                }
                this.finish()
                Toast.makeText(this, "Add Successfully !", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please field in all !", Toast.LENGTH_LONG).show()
            }
        }
    }
}