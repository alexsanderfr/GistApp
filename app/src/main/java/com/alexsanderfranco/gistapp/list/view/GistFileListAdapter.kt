package com.alexsanderfranco.gistapp.list.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexsanderfranco.gistapp.R
import com.alexsanderfranco.gistapp.database.entity.GistFile

class GistFileListAdapter : RecyclerView.Adapter<GistFileListAdapter.GistFileViewHolder>() {

    private val items: MutableList<GistFile> = mutableListOf()

    class GistFileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val filenameTextView: TextView = view.findViewById(R.id.gist_file_item_filename_tv)
        private val typeTextView: TextView = view.findViewById(R.id.gist_file_item_type_tv)

        fun bind(gistFile: GistFile) {
            view.tag = gistFile.filename
            filenameTextView.text = gistFile.filename
            typeTextView.text = gistFile.type
        }

    }

    fun update(gistFiles: List<GistFile>) {
        items.clear()
        items.addAll(gistFiles)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GistFileViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GistFileViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gist_file_item, parent, false)
    )

}