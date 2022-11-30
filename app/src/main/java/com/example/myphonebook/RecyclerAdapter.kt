package com.example.myphonebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    private var list: List<ContactEntity>,
    private val onItemClick: (id: Int) -> Unit
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = "${list[position].firstName}  ${list[position].lastName}"
        holder.button.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun updateAdapter(listUpdate:List<ContactEntity>){
        list = listUpdate
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.textView)
        val button = itemView.findViewById<TextView>(R.id.button)
    }
}