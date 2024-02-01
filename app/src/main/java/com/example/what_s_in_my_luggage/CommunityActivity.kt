package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        // ImageButton 참조
        val imageButton1: ImageButton = findViewById(R.id.co1)
        val imageButton2: ImageButton = findViewById(R.id.co2)
        val imageButton3: ImageButton = findViewById(R.id.co3)

        // ImageButton에 클릭 리스너 추가
        imageButton1.setOnClickListener(View.OnClickListener {
            // 새로운 액티비티로 이동하는 코드
            val intent = Intent(this@CommunityActivity, PostActivity::class.java)
            startActivity(intent)
        })

        imageButton2.setOnClickListener(View.OnClickListener {
            // 새로운 액티비티로 이동하는 코드
            val intent = Intent(this@CommunityActivity, PostActivity::class.java)
            startActivity(intent)
        })

        imageButton3.setOnClickListener(View.OnClickListener {
            // 새로운 액티비티로 이동하는 코드
            val intent = Intent(this@CommunityActivity, PostActivity::class.java)
            startActivity(intent)
        })
    }
}
