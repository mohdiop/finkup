package com.mohdiop.finkup.classes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohdiop.finkup.R
import com.mohdiop.finkup.database.Fink
import java.text.SimpleDateFormat
import java.util.Date

class FinkAdapter(private var context: Context, private var fink: List<Fink>) :
    RecyclerView.Adapter<FinkAdapter.FinkViewHolder>() {


    class FinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val content: TextView
        val date: TextView

        init {
            title = view.findViewById(R.id.finkTitle)
            content = view.findViewById(R.id.finkContent)
            date = view.findViewById(R.id.finkDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinkViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.fink_row, parent, false)
        return FinkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fink.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: FinkViewHolder, position: Int) {
        val newFink = fink[position]
        holder.title.text = newFink.finkTitle
        holder.content.text = newFink.finkContent
        holder.date.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date(newFink.finkDate))
    }

}