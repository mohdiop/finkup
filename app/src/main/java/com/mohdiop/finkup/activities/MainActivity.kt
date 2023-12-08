package com.mohdiop.finkup.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mohdiop.finkup.R
import com.mohdiop.finkup.classes.FinkAdapter
import com.mohdiop.finkup.database.FinkDao
import com.mohdiop.finkup.database.FinkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var add: ImageButton
    private lateinit var finkRecyclerView: RecyclerView

    private lateinit var finkDatabase: FinkDatabase
    private lateinit var finkDao: FinkDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listeners()
    }

    private fun init() {
        add = findViewById(R.id.add)
        finkRecyclerView = findViewById(R.id.finkRecycleView)
        finkDatabase = Room.databaseBuilder(this, FinkDatabase::class.java, "fink_database").build()
        finkDao = finkDatabase.finkDao()
        CoroutineScope(Dispatchers.Main).launch {
            val finkAdapter = FinkAdapter(applicationContext, finkDao.getAllFinks())
            finkRecyclerView.adapter = finkAdapter
            finkRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
            val dividerItemDecoration =
                DividerItemDecoration(applicationContext, RecyclerView.VERTICAL)
            ResourcesCompat.getDrawable(resources, R.drawable.fink_divider, null).let {
                dividerItemDecoration.setDrawable(it!!)
            }
            finkRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun listeners() {
        add.setOnClickListener {
            startActivity(Intent(this, AddFink::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        init()
        listeners()
    }
}