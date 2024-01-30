package com.example.what_s_in_my_luggage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding

class ItemListAdapter(var list: List<Items>): RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    class ItemListViewHolder(val binding: ActivityItemListBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemImageView.setOnClickListener {
                ItemList.onImageViewClick(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = ActivityItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        holder.binding.itemImageView.setImageResource(list[position].image)
//        holder.binding.itemNameTextView.text = list[position].name
    }

    override fun getItemCount(): Int {
        return list.size
    }


}