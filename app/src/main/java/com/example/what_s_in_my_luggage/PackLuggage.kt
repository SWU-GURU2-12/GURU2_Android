package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        lBinding.itemListRecyclerView.layoutManager = GridLayoutManager(this, 4)

        // 아이템 목록에 들어갈 아이템 객체 생성 및 어댑터 연결
        GetItemLists()

        // nextBtn 색상 관리
        if (ItemList.isItemExist) {
            lBinding.nextBtn.setTextColor(ContextCompat.getColor(this, ItemList.existTextColor))
        } else {
            lBinding.nextBtn.setTextColor(ContextCompat.getColor(this, ItemList.notExistTextColor))
        }

        // 리스트 버튼을 눌렀을 때
        // 아이템이 한 개 이상 추가되었다면  체크리스트 페이지로 이동
        // 아이템을 추가하지 않았다면 페이지 이동X 및 토스트 메시지 띄움
        lBinding.nextBtn.setOnClickListener {
            if(ItemList.isItemExist) {
                val checklistIntent = Intent(this, Checklist::class.java)
                startActivity(checklistIntent)
            } else {
                Toast.makeText(applicationContext, "아이템을 한 개 이상 추가해주세요", Toast.LENGTH_SHORT).show()
            }
        }

//        lBinding.nextBtn.setTextColor(nextBtnColor)

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

    fun GetItemLists() {
        val num = 0

        // Firebase Storage 관리 객체 소환
        val firebaseStorage = FirebaseStorage.getInstance()

        // 저장소의 최상위 참조 객체 얻어오기
        val rootRef = firebaseStorage.reference

        // StorageReference 리스트 생성
        val imageRefs: List<StorageReference> = (1..15).map { index ->
            rootRef.child("items/item_image_removebg_${num + index}.png")
        }

        // 아이템 목록 생성
        val allItems: List<Items> = listOf(
            Items(imageRefs[0], "어댑터"),
            Items(imageRefs[1], "카메라"),
            Items(imageRefs[2], "보조배터리"),
            Items(imageRefs[3], "여권"),
            Items(imageRefs[4], "개인 가방"),
            Items(imageRefs[5], "유럽 돈"),
            Items(imageRefs[6], "겨울 상의"),
            Items(imageRefs[7], "겨울 하의"),
            Items(imageRefs[8], "머플러"),
            Items(imageRefs[9], "모자"),
            Items(imageRefs[10], "부츠"),
            Items(imageRefs[11], "화장품"),
            Items(imageRefs[12], "칫솔&치약"),
            Items(imageRefs[13], "스킨케어"),
            Items(imageRefs[14], "컵라면"),
        )

        val electronics: List<Items> = listOf(
            Items(imageRefs[0], "어댑터"),
            Items(imageRefs[1], "카메라"),
            Items(imageRefs[2], "보조배터리")
        )

        val inFlightEssentials: List<Items> = listOf(
            Items(imageRefs[3], "여권"),
            Items(imageRefs[2], "보조배터리"),
            Items(imageRefs[4], "개인 가방"),
            Items(imageRefs[5], "현금")
        )

        val clothes: List<Items> = listOf(
            Items(imageRefs[6], "겨울 상의"),
            Items(imageRefs[7], "겨울 하의")
        )

        val otherClothes: List<Items> = listOf(
            Items(imageRefs[8], "머플러"),
            Items(imageRefs[9], "모자"),
            Items(imageRefs[10], "부츠")
        )

        val care: List<Items> = listOf(
            Items(imageRefs[11], "화장품"),
            Items(imageRefs[12], "칫솔&치약"),
            Items(imageRefs[13], "스킨케어")
        )

        val food: List<Items> = listOf(
            Items(imageRefs[14], "컵라면")
        )

        // 각 아이템 목록을 버튼에 연결
        lBinding.allItemsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(allItems, this)
        }

        lBinding.electronicsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(electronics, this)
        }

        lBinding.inFlightEssentialsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(inFlightEssentials, this)
        }

        lBinding.clothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(clothes, this)
        }

        lBinding.otherClothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(otherClothes, this)
        }

        lBinding.careBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(care, this)
        }

        lBinding.foodBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(food, this)
        }
    }
}