package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.what_s_in_my_luggage.databinding.ActivityChecklistBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Checklist : AppCompatActivity() {
    private lateinit var cBinding: ActivityChecklistBinding
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cBinding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(cBinding.root)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 뒤로가기 버튼 누르면 짐꾸리기 페이지로 이동
        cBinding.backBtn.setOnClickListener {
            val intent = Intent(this, PackLuggage::class.java)
            startActivity(intent)
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