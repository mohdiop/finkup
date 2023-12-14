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
import com.mohdiop.finkup.database.FinkApiController
import com.mohdiop.finkup.database.FinkDao
import com.mohdiop.finkup.database.FinkDatabase
import com.mohdiop.finkup.database.FinkRetrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity(), FinkListener {

    private lateinit var addFink: ImageButton
    private lateinit var finkRecyclerView: RecyclerView
    private lateinit var totalFinkNumber: TextView
    private lateinit var delete: ImageButton
    private lateinit var refresh: ImageButton
    private lateinit var searchFink: androidx.appcompat.widget.SearchView

    private lateinit var finkDatabase: FinkDatabase
    private lateinit var finkDao: FinkDao
    private lateinit var finkRetrofit: FinkApiController
    private lateinit var dividerItemDecoration: DividerItemDecoration

    companion object {
        var savedInServer: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        listeners()
        loadDataFromServer()
        CoroutineScope(Dispatchers.Main).launch {
            loadView(finkDao.getAllFinks())
        }
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
        finkRetrofit = FinkRetrofit.getInstance()
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
                        loadView(finkDao.getAllFinks())
                        CoroutineScope(Dispatchers.IO).launch {
                            finkRetrofit.deleteAllFinks().enqueue(object : Callback<ResponseBody?> {
                                override fun onResponse(
                                    call: Call<ResponseBody?>,
                                    response: Response<ResponseBody?>
                                ) {
                                    makeToast(response.body()!!.string())
                                }

                                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                                    makeToast(t.message.toString())
                                }
                            })
                        }
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
            makeToast("Refreshing ...")
            loadDataFromServer()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadView(finkToView: List<Fink>) {
        val finkAdapter = FinkAdapter(applicationContext, finkToView, this)
        finkRecyclerView.adapter = finkAdapter
        val numberOfFinks = finkToView.size
        totalFinkNumber.text = "$numberOfFinks finks in total."
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

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            loadView(finkDao.getAllFinks())
        }
        if (!savedInServer) {
            saveDataInServer()
            savedInServer = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (!savedInServer) {
            saveDataInServer()
            savedInServer = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!savedInServer) {
            saveDataInServer()
            savedInServer = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (!savedInServer) {
            saveDataInServer()
            savedInServer = true
        }
    }

    private fun saveDataInServer() {
        finkRetrofit.deleteAllFinks().enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    CoroutineScope(Dispatchers.IO).launch {
                        finkRetrofit.addAllFinks(finkDao.getAllFinks())
                            .enqueue(object : Callback<ResponseBody?> {
                                override fun onResponse(
                                    call: Call<ResponseBody?>,
                                    response: Response<ResponseBody?>
                                ) {
                                    println(response.body()!!.string())
                                }

                                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                                    println(t.message)
                                }
                            })
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                makeToast(t.message.toString())
            }
        })
    }

    private fun loadDataFromServer() {
        CoroutineScope(Dispatchers.IO).launch {
            finkRetrofit.getAllFinks().enqueue(object : Callback<List<Fink>?> {
                override fun onResponse(call: Call<List<Fink>?>, response: Response<List<Fink>?>) {
                    if (response.isSuccessful) {
                        CoroutineScope(Dispatchers.Main).launch {
                            finkDao.truncate()
                            for (fink in response.body()!!) {
                                finkDao.insertFink(fink)
                            }
                            loadView(finkDao.getAllFinks())
                        }
                    } else {
                        makeToast("Unable to access data from server")
                    }
                }

                override fun onFailure(call: Call<List<Fink>?>, t: Throwable) {
                    makeToast(t.message.toString())
                }
            })
        }
    }

    fun makeToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }
}