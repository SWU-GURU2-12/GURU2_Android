package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CheckListFragment : Fragment() {
    var packingFrameActivity: PackingFrameActivity? = null

    private lateinit var electronicsLayout: LinearLayout
    private lateinit var inFlightEssentialsLayout: LinearLayout
    private lateinit var clothesLayout: LinearLayout
    private lateinit var otherColthesLayout: LinearLayout
    private lateinit var careLayout: LinearLayout
    private lateinit var foodLayout: LinearLayout
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_check_list, container, false)

        electronicsLayout = view.findViewById<LinearLayout>(R.id.electronicsLayout)
        inFlightEssentialsLayout = view.findViewById<LinearLayout>(R.id.inFlightEssentialsLayout)
        clothesLayout = view.findViewById<LinearLayout>(R.id.clothesLayout)
        otherColthesLayout = view.findViewById<LinearLayout>(R.id.otherColthesLayout)
        careLayout = view.findViewById<LinearLayout>(R.id.careLayout)
        foodLayout = view.findViewById<LinearLayout>(R.id.foodLayout)

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

        return view
    }

    private fun displayChecklist(itemList: List<String>) {
        // 가져온 데이터로 동적으로 TextView 생성하여 추가
        for (item in itemList) {
            val textView = TextView(requireContext())
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
            val textColor = ContextCompat.getColor(requireContext(), R.color.bb70)
            textView.setTextColor(textColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

            // 생성한 TextView를 알맞는 Layout에 추가
            when (item) {
                "어댑터", "카메라", "보조배터리" -> {
                    electronicsLayout.addView(textView)
                }
                "여권", "개인 가방", "유럽 돈" -> {
                    inFlightEssentialsLayout.addView(textView)
                }
                "겨울 상의", "겨울 하의" -> {
                    clothesLayout.addView(textView)
                }
                "머플러", "모자", "부츠" -> {
                    otherColthesLayout.addView(textView)
                }
                "화장품", "칫솔&치약", "스킨케어" -> {
                    careLayout.addView(textView)
                }
                "컵라면" -> {
                    foodLayout.addView(textView)
                }
            }
        }
    }
}