package com.example.what_s_in_my_luggage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding
import com.google.firebase.database.FirebaseDatabase

class ItemListAdapter(var list: List<Items>): RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    inner class ItemListViewHolder(val binding: ActivityItemListBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemImageView.setOnClickListener {
                ItemList.onImageViewClick(it)

                val clickedItem = list[adapterPosition]
                sendDataToFirebase(clickedItem)
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

    private fun sendDataToFirebase(item: Items) {
        // 클릭된 아이템에 대한 데이터 전송
        val databaseRef = FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung")

        // 전송할 데이터 생성
        val dataToAdd = mapOf(
            "itemName" to item.name,
            // 추가하려는 다른 데이터 필드들을 추가
        )

        // DatabaseReference의 push 메서드를 사용하여 데이터를 자동 생성된 고유 키에 추가
        databaseRef.child("luggage1").push().setValue(dataToAdd)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}