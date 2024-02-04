package com.example.what_s_in_my_luggage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding

class ItemListAdapter(var list: List<Items>, private val context: Context): RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    inner class ItemListViewHolder(val binding: ActivityItemListBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemImageView.setOnClickListener {
                val clickedItem = list[adapterPosition]

                ItemList.onImageViewClick(it, clickedItem, context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = ActivityItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val storageReference = list[position].image

        // StorageReference에서 downloadUrl 가져오기
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            // Glide를 사용하여 이미지 로드
            Glide.with(context)
                .load(uri)
                .into(holder.binding.itemImageView)
        }.addOnFailureListener { exception ->
            // 실패할 경우 처리
            Log.e("ItemListAdapter", "Error getting download URL: ${exception.message}")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}