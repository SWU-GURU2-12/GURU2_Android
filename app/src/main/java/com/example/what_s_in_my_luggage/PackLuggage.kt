package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.what_s_in_my_luggage.databinding.ActivityPackLuggageBinding

class PackLuggage : AppCompatActivity() {
    lateinit var lBinding: ActivityPackLuggageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lBinding = ActivityPackLuggageBinding.inflate(layoutInflater)
        setContentView(lBinding.root)

        // 아이템 목록 구현을 위해 RecyclerView 연결
//        lBinding.itemListRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        lBinding.itemListRecyclerView.layoutManager = GridLayoutManager(this, 2)
        lBinding.itemListRecyclerView.addItemDecoration((DividerItemDecoration(this, DividerItemDecoration.VERTICAL)))

        GetItemLists()

        // 리스트 버튼을 누르면 체크리스트 페이지로 이동
        lBinding.listBtn.setOnClickListener {
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

        // 저장 버튼을 누르면 경고메시지 발생
        lBinding.saveBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("저장하시겠습니까?")
                .setPositiveButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext, "예 선택(저장)", Toast.LENGTH_SHORT).show()
                        // 이후에 MyRoom 페이지 연결
                    })
                .setNegativeButton("아니요",
                    DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(applicationContext, "아니요 선택(저장)", Toast.LENGTH_SHORT).show()
                        // 예 버튼 작성 끝나면 토스트 메시지 코드 삭제
                    })
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    // 아이템 목록에 들어갈 아이템 객체 생성 및 어댑터 연결
    fun GetItemLists() {
        var clothes: List<Items> = listOf(
            Items(R.drawable.free_icon_tshirt_2806248, "상의(여름용)"),
            Items(R.drawable.free_icon_long_pants_11386531, "하의(겨울용)"),
            Items(R.drawable.free_icon_sun_glass_7087908, "선글라스")
        )

        var electronics: List<Items> = listOf(
            Items(R.drawable.free_icon_ipad_12355597, "스마트패드"),
            Items(R.drawable.free_icon_earphone_5906124, "이어폰"),
            Items(R.drawable.free_icon_headphone_4439623, "헤드폰")
        )

        lBinding.clothesBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(clothes)
        }
        lBinding.electronicsBtn.setOnClickListener {
            lBinding.itemListRecyclerView.adapter = ItemListAdapter(electronics)
        }
    }
}