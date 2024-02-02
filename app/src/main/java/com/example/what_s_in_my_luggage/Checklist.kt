package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.what_s_in_my_luggage.databinding.ActivityChecklistBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Checklist : AppCompatActivity() {
    private lateinit var cBinding: ActivityChecklistBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cBinding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(cBinding.root)

        val nextBtn = findViewById<Button>(R.id.nextBtn)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 뒤로가기 버튼 누르면 짐꾸리기 페이지로 이동
        // 짐의 구성은 저장되지 않음!!
        cBinding.backBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("짐 꾸리기 페이지로 돌아가시겠습니까?\n(저장되지 않습니다.)")
                .setPositiveButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
//                        Toast.makeText(applicationContext, "예 선택(뒤로가기)", Toast.LENGTH_SHORT).show()
                        // 이후에 MyRoom 페이지 연결
                        val intent = Intent(this, PackLuggage::class.java)
                        startActivity(intent)
//                        ItemList.isItemsLoaded = false

                        // 저장된 luggage와 screenshot 삭제
                        removeLuggageAndScreenshotFromFirebase()

                        ItemList.isItemExist = false
                        nextBtn.setTextColor(ItemList.notExistTextColor)
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext, "아니요 선택(뒤로가기)", Toast.LENGTH_SHORT).show()
                        // 예 버튼 작성 끝나면 토스트 메시지 코드 삭제
                    })
            val alertDialog = builder.create()
            alertDialog.show()
        }

        // 저장 버튼 누르면 짐꾸리기 페이지로 이동
        cBinding.nextBtn.setOnClickListener {
            // 발행하기 페이지랑 연결
//            val intent = Intent(this, PackLuggage::class.java)
//            startActivity(intent)
//            ItemList.isItemsLoaded = false
        }

        // 추가된 아이템을 체크리스트에 표시
        // Firebase에서 데이터 가져오기
        // checklist 그룹 -> 사용자 이름 그룹 -> 여행 이름 그룹
        databaseRef = FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung").child("luggage1")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // dataSnapshot에서 데이터 가져오기
                val itemList = mutableListOf<String>()
                for (itemSnapshot in snapshot.children) {
                    val itemName = itemSnapshot.child("itemName").getValue(String::class.java)
                    itemName?.let {
                        itemList.add(it)
                    }
                }

                // TextView 동적으로 생성하여 추가
                displayChecklist(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
            }
        })
    }

    private fun removeLuggageAndScreenshotFromFirebase() {
        val captureFileName = intent.getStringExtra("captureFileName")

        // Remove "luggage1" data from Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung").child("luggage1")
        databaseRef.removeValue()

        // Remove screenshot from Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("captures/$captureFileName.jpg")

        imagesRef.delete().addOnSuccessListener {
            // Deletion success
            Log.d("Firebase", "Screenshot deleted successfully")
        }.addOnFailureListener {
            // Deletion failed
            Log.e("Firebase", "Failed to delete screenshot: $it")
        }
    }

    private fun displayChecklist(itemList: List<String>) {
        // 가져온 데이터로 동적으로 TextView 생성하여 추가
        for (item in itemList) {
            val textView = TextView(this)
            textView.text = item

            // 새로운 LayoutParams를 생성하여 설정
            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // margin 설정 (top: 16dp)
            layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.margin_top)

            // 생성한 LayoutParams를 TextView에 설정
            textView.layoutParams = layoutParams

            // 필요에 따라 TextView의 스타일이나 속성을 설정할 수 있습니다.
            val textColor = ContextCompat.getColor(this, R.color.bb70)
            textView.setTextColor(textColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

            // 생성한 TextView를 알맞는 Layout에 추가
            when (item) {
                "어댑터", "카메라", "보조배터리" -> {
                    cBinding.electronicsLayout.addView(textView)
                }
                "여권", "개인 가방", "유럽 돈" -> {
                    cBinding.inFlightEssentialsLayout.addView(textView)
                }
                "겨울 상의", "겨울 하의" -> {
                    cBinding.clothesLayout.addView(textView)
                }
                "머플러", "모자", "부츠" -> {
                    cBinding.otherColthesLayout.addView(textView)
                }
                "화장품", "칫솔&치약", "스킨케어" -> {
                    cBinding.careLayout.addView(textView)
                }
                "컵라면" -> {
                    cBinding.foodLayout.addView(textView)
                }
            }
        }
    }
}