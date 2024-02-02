package com.example.what_s_in_my_luggage

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MyRoomActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var tvProfileName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room)

        // 시스템 UI 가시성 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // 바텀 네비게이션 설정
        setupBottomNavigation()

        // Firebase Database 참조 초기화 및 사용자 닉네임 불러오기
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        tvProfileName = findViewById(R.id.tvProfileName)

        // Intent에서 사용자 키 값 가져오기
        val userKey = intent.getStringExtra("USER_KEY")
        userKey?.let {
            loadUserNickname(it)
        }
    }

    private fun setupBottomNavigation() {
        val sharedPrefs = getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        val selectedButtonId = sharedPrefs.getInt("SELECTED_BUTTON_ID", R.id.btnNaviMyroom)
        val bottomNavFragment = BottomNavigationFragment.newInstance(selectedButtonId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavigationFragment, bottomNavFragment)
            .commit()
    }

    private fun loadUserNickname(userKey: String) {
        // 사용자 키를 사용하여 Firebase Database에서 닉네임 검색
        databaseReference.child(userKey).child("nickname").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nickname = snapshot.getValue(String::class.java)
                    tvProfileName.text = nickname ?: "Unknown"
                } else {
                    Toast.makeText(baseContext, "닉네임을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(baseContext, "데이터베이스 오류: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
