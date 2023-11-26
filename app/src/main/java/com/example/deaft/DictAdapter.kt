package com.example.deaft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DictAdapter(private val itemList: List<String>) : RecyclerView.Adapter<DictAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView = itemView.findViewById(R.id.textItem)
        val btnIcon: ImageButton = itemView.findViewById(R.id.btnIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dict_words, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textItem.text = currentItem

        holder.btnIcon.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Botão clicado para o item: $currentItem", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = itemList.size
}
