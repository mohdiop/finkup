package com.mohdiop.finkup.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var addFink: ImageButton
    private lateinit var finkRecyclerView: RecyclerView
    private lateinit var totalFinkNumber: TextView
    private lateinit var delete: ImageButton
    private lateinit var refresh: ImageButton
    private lateinit var searchFink: androidx.appcompat.widget.SearchView

    private lateinit var finkDatabase: FinkDatabase
    private lateinit var finkDao: FinkDao
    private lateinit var dividerItemDecoration: DividerItemDecoration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listeners()
    }

    private fun init() {
        addFink = findViewById(R.id.add)
        finkRecyclerView = findViewById(R.id.finkRecycleView)
        searchFink = findViewById(R.id.finkSearch)
        delete = findViewById(R.id.delete)
        refresh = findViewById(R.id.refresh)
        dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.fink_divider, null).let {
            dividerItemDecoration.setDrawable(it!!)
        }
        finkRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        finkRecyclerView.addItemDecoration(dividerItemDecoration)
        totalFinkNumber = findViewById(R.id.totalFinkNumber)
        finkDatabase = FinkDatabase.getInstance(applicationContext)
        finkDao = finkDatabase.finkDao()
        CoroutineScope(Dispatchers.Main).launch {
            loadView(finkDao.getAllFinks())
        }
    }

    private fun listeners() {
        addFink.setOnClickListener {
            startActivity(Intent(this, AddFink::class.java))
        }
        searchFink.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    loadView(finkDao.searchFink(query!!))
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Dispatchers.Main).launch {
                    if (newText!!.isEmpty()) {
                        loadView(finkDao.getAllFinks())
                    } else {
                        loadView(finkDao.searchFink(newText))
                    }
                }
                return true
            }
        })
        delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete all finks !!!")
                .setMessage("Are you sure to delete all the finks?")
                .setPositiveButton("YES") { _, _ ->
                    CoroutineScope(Dispatchers.Main).launch {
                        finkDao.truncate()
                        Toast.makeText(
                            this@MainActivity,
                            "All finks are deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadView(finkDao.getAllFinks())
                    }
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            CoroutineScope(Dispatchers.Main).launch {
                loadView(finkDao.getAllFinks())
            }
        }
        refresh.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@MainActivity, "Refreshing ...", Toast.LENGTH_SHORT).show()
                loadView(finkDao.getAllFinks())
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun loadView(finkToView: List<Fink>) {
        val finkAdapter = FinkAdapter(applicationContext, finkToView, this)
        finkRecyclerView.adapter = finkAdapter
        val numberOfFinks = finkToView.size
        totalFinkNumber.text = "$numberOfFinks finks in total."
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            loadView(finkDao.getAllFinks())
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

    override fun onFinkLongClickListener(fink: Fink): Boolean {
        AlertDialog.Builder(this).setTitle(fink.finkTitle)
            .setNegativeButton("Delete") { _, _ ->
                CoroutineScope(Dispatchers.Main).launch {
                    finkDao.deleteFink(fink)
                    loadView(finkDao.getAllFinks())
                }
            }
            .setPositiveButton("View") { _, _ ->
                this.onFinkClickListener(fink)
            }
            .create()
            .show()
        return true
    }
}