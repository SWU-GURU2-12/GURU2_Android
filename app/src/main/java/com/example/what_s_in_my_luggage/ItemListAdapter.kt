package com.example.what_s_in_my_luggage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class ItemListAdapter(var list: List<Items>, private val context: Context): RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>() {

    inner class ItemListViewHolder(val binding: ActivityItemListBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemImageView.setOnClickListener {
                val clickedItem = list[adapterPosition]

                ItemList.onImageViewClick(it, clickedItem)
                sendDataToFirebase(clickedItem)
            }
        }
    }

//    data class Items(val name: String, val image: StorageReference, var x: Float = 0f, var y: Float = 0f)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val binding = ActivityItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
//        holder.binding.itemImageView.setImageResource(list[position].image)
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

    private fun sendDataToFirebase(item: Items) {
        // 클릭된 아이템에 대한 데이터 전송
        val databaseRef = FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung").child("luggage1")

        // 전송할 데이터 생성
        val dataToAdd = mapOf(
            "itemName" to item.name,
//            "itemX" to ItemList.itemX,
//            "itemY" to ItemList.itemY
            "itemX" to item.x,
            "itemY" to item.y
            // 추가하려는 다른 데이터 필드들을 추가
        )

        // DatabaseReference의 push 메서드를 사용하여 데이터를 자동 생성된 고유 키에 추가
        databaseRef.push().setValue(dataToAdd)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}