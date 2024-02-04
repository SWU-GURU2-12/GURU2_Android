package com.example.what_s_in_my_luggage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback

class PostActivity : AppCompatActivity() {

    lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        btnBack = findViewById(R.id.btnBack)

        // OnBackPressedCallback을 생성하여 뒤로 가기 이벤트를 처리
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

        btnBack.setOnClickListener {
            // Unresolved reference 오류 수정: 직접 클래스 내부의 함수를 호출
            this.handleOnBackPressed()
        }

        // OnBackPressedCallback을 등록
        onBackPressedDispatcher.addCallback(this, callback)
    }

    // handleOnBackPressed 함수를 클래스 내에 추가
    private fun handleOnBackPressed() {
        finish()
    }
}