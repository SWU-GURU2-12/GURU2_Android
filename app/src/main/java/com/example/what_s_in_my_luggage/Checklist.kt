package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.what_s_in_my_luggage.databinding.ActivityChecklistBinding

class Checklist : AppCompatActivity() {
    lateinit var cBinding: ActivityChecklistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cBinding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(cBinding.root)

        // 뒤로가기 버튼 누르면 짐꾸리기 페이지로 이동
        cBinding.checklistBackBtn.setOnClickListener {
            val intent = Intent(this, PackLuggage::class.java)
            startActivity(intent)
        }

        // 체크리스트 목록의 체크 버튼 누르면 체크되고 다시 누르면 해제됨
        cBinding.checkedTextView.setOnClickListener {
            cBinding.checkedTextView.toggle()
        }
    }
}