package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.what_s_in_my_luggage.databinding.ActivityPackLuggageBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PackLuggage : AppCompatActivity() {
    lateinit var lBinding: ActivityPackLuggageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lBinding = ActivityPackLuggageBinding.inflate(layoutInflater)
        setContentView(lBinding.root)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 아이템 목록 구현을 위해 RecyclerView 연결
//        lBinding.itemListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        lBinding.itemListRecyclerView.layoutManager = GridLayoutManager(this, 4)
//        lBinding.itemListRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        GetItemLists()

        // 리스트 버튼을 누르면 체크리스트 페이지로 이동
        lBinding.nextBtn.setOnClickListener {
            val intent = Intent(this, Checklist::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼을 누르면 경고메시지 발생
        lBinding.backBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("작성을 취소하고 My room으로 돌아가시겠습니까?")
                .setPositiveButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext, "예 선택(뒤로가기)", Toast.LENGTH_SHORT).show()
                        // 이후에 MyRoom 페이지 연결
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext, "아니요 선택(뒤로가기)", Toast.LENGTH_SHORT).show()
                        // 예 버튼 작성 끝나면 토스트 메시지 코드 삭제
                    })
            val alertDialog = builder.create()
            alertDialog.show()
        }

//        // 저장 버튼을 누르면 경고메시지 발생
//        lBinding.nextBtn.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setMessage("저장하시겠습니까?")
//                .setPositiveButton("예",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        Toast.makeText(applicationContext, "예 선택(저장)", Toast.LENGTH_SHORT).show()
//                        // 이후에 MyRoom 페이지 연결
//                    })
//                .setNegativeButton("아니요",
//                    DialogInterface.OnClickListener { dialog, which ->
//                        Toast.makeText(applicationContext, "아니요 선택(저장)", Toast.LENGTH_SHORT).show()
//                        // 예 버튼 작성 끝나면 토스트 메시지 코드 삭제
//                    })
//            val alertDialog = builder.create()
//            alertDialog.show()
//        }
    }

    // 아이템 목록에 들어갈 아이템 객체 생성 및 어댑터 연결
    fun GetItemLists() {
        val num = 0

        // Firebase Storage 관리 객체 소환
        val firebaseStorage = FirebaseStorage.getInstance()

        // 저장소의 최상위 참조 객체 얻어오기
        val rootRef = firebaseStorage.reference

        // StorageReference 리스트 생성
        val imageRefs: List<StorageReference> = (1..15).map { index ->
            rootRef.child("items/item_image_${num + index}.png")
        }

        val clothes: List<Items> = listOf(
            Items(imageRefs[6], "겨울 상의"),
            Items(imageRefs[7], "겨울 하의")
//            Items(resources.getIdentifier("free_icon_headphone_${num}", "drawable", packageName), "헤드폰~")
        )

        val electronics: List<Items> = listOf(
            Items(imageRefs[0], "어댑터"),
            Items(imageRefs[1], "카메라"),
            Items(imageRefs[2], "보조배터리")
        )

//        // Firebase Storage 관리 객체 소환
//        val firebaseStorage = FirebaseStorage.getInstance()
//
//        // 저장소의 최상위 참조 객체 얻어오기
//        val rootRef = firebaseStorage.reference
//
//        // 읽어오길 원하는 파일의 참조 객체 얻어오기 (예시로 "free_icon_headphone_${num}.png" 파일을 가정)
//        val imgRef = rootRef.child("images/item_image_${num + 1}.png")

        lBinding.clothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(clothes, this)
        }
        lBinding.electronicsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(electronics, this)
        }
    }
}