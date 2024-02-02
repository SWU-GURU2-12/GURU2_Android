package com.example.what_s_in_my_luggage

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.what_s_in_my_luggage.databinding.ActivityPackLuggageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PackLuggage : AppCompatActivity() {
    lateinit var lBinding: ActivityPackLuggageBinding
    lateinit var itemAdapter: ItemListAdapter

//    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lBinding = ActivityPackLuggageBinding.inflate(layoutInflater)
        setContentView(lBinding.root)


        // 액션바 숨기기
        supportActionBar?.hide()

        // 아이템 목록 구현을 위해 RecyclerView 연결
        lBinding.itemListRecyclerView.layoutManager = GridLayoutManager(this, 4)

        // 아이템 목록에 들어갈 아이템 객체 생성 및 어댑터 연결
        getItemListAdapter()

        // nextBtn 색상 관리
        if (ItemList.isItemExist) {
            lBinding.nextBtn.setTextColor(ContextCompat.getColor(this, ItemList.existTextColor))
        } else {
            lBinding.nextBtn.setTextColor(ContextCompat.getColor(this, ItemList.notExistTextColor))
        }

        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val time = Date()
        val currentTime: String = sdf.format(time)

        // 다음 버튼을 눌렀을 때
        // 아이템이 한 개 이상 추가되었다면 체크리스트 페이지로 이동 & 캐리어 캡쳐 및 저장
        // 아이템을 추가하지 않았다면 페이지 이동X 및 토스트 메시지 띄움
        lBinding.nextBtn.setOnClickListener {
            if(ItemList.isItemExist) {

                // 짐 꾸린 캐리어 캡쳐
                requestCapture(lBinding.luggageLayout, "$currentTime\\_capture")

                val checklistIntent = Intent(this, Checklist::class.java)
                checklistIntent.putExtra("captureFileName", "$currentTime\\_capture") // 스크린샷 파일명 전달
                startActivity(checklistIntent)
            } else {
                Toast.makeText(applicationContext, "아이템을 한 개 이상 추가해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로가기 버튼을 누르면 경고메시지 발생
        lBinding.backBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("작성을 취소하고 My Room으로 돌아가시겠습니까?")
                .setPositiveButton("예") { dialog, which ->
                    Toast.makeText(applicationContext, "예 선택(뒤로가기)", Toast.LENGTH_SHORT).show()

                    // 만들던 luggage의 DB 삭제


                    // 이후에 MyRoom 페이지 연결
                    // 왜 연결이 안되지????
//                    val myRoomIntent = Intent(this, MyRoomActivity::class.java)
//                    startActivity(myRoomIntent)
                }
                .setNegativeButton("아니요") { dialog, which ->
                    Toast.makeText(applicationContext, "아니요 선택(뒤로가기)", Toast.LENGTH_SHORT).show()
                    // 예 버튼 작성 끝나면 토스트 메시지 코드 삭제
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun requestCapture(view: View, fileName: String) {
        if (view == null) {
            println("::::ERROR:::: captureTargetLayout == NULL")
            return
        }

        // 캐시 비트맵 만들기
        view.buildDrawingCache()
        val bitmap: Bitmap = view.drawingCache

        // Firebase Storage에 이미지 업로드
        var dataManager = UserDataManager.getInstance(this)
        dataManager.uploadImageToFirebaseStorage(bitmap, fileName)
    }

    private fun getItemListAdapter() {
        // Glide에 전달할 Context 저장
        val contextForGlide = this
        var dataManager = UserDataManager.getInstance(this)
        dataManager.setItemLists()
        Log.d("itemlist_111","ok")

        // 짐꾸리기 페이지 로딩되면 전체 메뉴 바로 보여줌
        itemAdapter = ItemListAdapter(dataManager.allItems, contextForGlide)
        Log.d("itemlist_222","ok")
        lBinding.itemListRecyclerView.adapter = itemAdapter
        Log.d("itemlist_333","ok")

        // 각 아이템 목록을 버튼에 연결
        lBinding.allItemsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.allItems, contextForGlide)
        }
        Log.d("itemlist_4","ok")

        lBinding.electronicsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.electronics, contextForGlide)
        }
        Log.d("itemlist_5","ok")

        lBinding.inFlightEssentialsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.inFlightEssentials, contextForGlide)
        }
        Log.d("itemlist_6","ok")

        lBinding.clothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.clothes, contextForGlide)
        }
        Log.d("itemlist_7","ok")

        lBinding.otherClothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.otherClothes, contextForGlide)
        }
        Log.d("itemlist_8","ok")

        lBinding.careBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.care, contextForGlide)
        }
        Log.d("itemlist_9","ok")

        lBinding.foodBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(dataManager.food, contextForGlide)
        }
        Log.d("itemlist_10","ok")
    }
}
