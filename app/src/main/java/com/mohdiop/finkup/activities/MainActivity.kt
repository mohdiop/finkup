package com.mohdiop.finkup.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohdiop.finkup.R
import com.mohdiop.finkup.classes.FinkAdapter
import com.mohdiop.finkup.classes.FinkListener
import com.mohdiop.finkup.database.Fink
import com.mohdiop.finkup.database.FinkDao
import com.mohdiop.finkup.database.FinkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), FinkListener {

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
        finkDatabase = FinkDatabase.getInstance(applicationContext)
        finkDao = finkDatabase.finkDao()
        loadView()
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

    private fun loadView() {
        CoroutineScope(Dispatchers.Main).launch {
            val finkAdapter =
                FinkAdapter(applicationContext, finkDao.getAllFinks(), this@MainActivity)
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

    override fun onFinkClickListener(fink: Fink) {
        intent = Intent(applicationContext, ViewFink::class.java)
        intent.putExtra("finkTitle", fink.finkTitle)
        intent.putExtra("finkContent", fink.finkContent)
        intent.putExtra("finkId", fink.finkId)
        intent.putExtra("finkDate", fink.finkDate)
        startActivity(intent)
    }
}