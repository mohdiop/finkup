package com.mohdiop.finkup.activities

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mohdiop.finkup.R
import com.mohdiop.finkup.database.Fink
import com.mohdiop.finkup.database.FinkDao
import com.mohdiop.finkup.database.FinkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewFink : AppCompatActivity() {

    private lateinit var back: ImageButton
    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var update: Button
    private lateinit var finkToView: Fink

    private lateinit var finkDatabase: FinkDatabase
    private lateinit var finkDao: FinkDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_fink)
        init()
        listeners()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= 29) {
            window.statusBarColor =
                ContextCompat.getColor(this, R.color.first)
            window.navigationBarColor =
                ContextCompat.getColor(this, R.color.grey)
        }
        back = findViewById(R.id.backToMainView)
        title = findViewById(R.id.titleView)
        content = findViewById(R.id.contentView)
        update = findViewById(R.id.updateFink)
        finkDatabase = FinkDatabase.getInstance(applicationContext)
        finkDao = finkDatabase.finkDao()
        finkToView = Fink(
            finkId = this.intent.getLongExtra("finkId", 0),
            finkTitle = this.intent.getStringExtra("finkTitle")!!,
            finkContent = this.intent.getStringExtra("finkContent")!!,
            finkDate = this.intent.getLongExtra("finkDate", 0)
        )
        title.setText(finkToView.finkTitle)
        content.setText(finkToView.finkContent)
    }

    private fun listeners() {
        back.setOnClickListener {
            this.finish()
        }
        update.setOnClickListener {
            when (update.text.toString()) {
                getString(R.string.update) -> {
                    title.isEnabled = true
                    content.isEnabled = true
                    update.text = getString(R.string.done)

                }

                getString(R.string.done) -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        update(
                            Fink(
                                finkId = finkToView.finkId,
                                finkTitle = title.text.toString(),
                                finkContent = content.text.toString(),
                                finkDate = System.currentTimeMillis()
                            )
                        )
                    }
                    title.isEnabled = false
                    content.isEnabled = false
                    update.text = getString(R.string.update)
                    Toast.makeText(applicationContext, "Update Successfully !", Toast.LENGTH_LONG)
                        .show()
                    Home.savedInServer = false
                }
            }
        }
    }

    private suspend fun update(fink: Fink) {
        finkDao.updateFink(fink)
    }
}