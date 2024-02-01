package com.example.what_s_in_my_luggage

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var btnGetCarrying: ImageButton
    lateinit var btnGetMyroom: ImageButton
    lateinit var btnGetSomeones: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // 앱이 시작될 때마다 선택된 버튼을 MainActivity 버튼으로 초기화
        val sharedPrefs = getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt("SELECTED_BUTTON_ID", R.id.btnNaviMain)
            apply()
        }

        // 바텀 네비게이션 프래그먼트 생성 및 초기 선택된 버튼 ID 전달
        val bottomNavFragment = BottomNavigationFragment.newInstance(R.id.btnNaviMain)
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavigationFragment, bottomNavFragment)
            .commit()

        btnGetCarrying = findViewById<ImageButton>(R.id.btnGetCarrying)
        btnGetMyroom = findViewById<ImageButton>(R.id.btnGetMyroom)
        btnGetSomeones = findViewById<ImageButton>(R.id.btnGetSomeones)

        // btnGetCarrying 버튼에 대한 클릭 리스너 설정
        btnGetCarrying.setOnClickListener {
            navigateToActivityWithUserKey(PackLuggage::class.java)
        }

        // btnGetMyroom 버튼에 대한 클릭 리스너 설정
        btnGetMyroom.setOnClickListener {
            navigateToActivityWithUserKey(MyRoomActivity::class.java)
        }

        // btnGetSomeones 버튼에 대한 클릭 리스너 설정
        btnGetSomeones.setOnClickListener {
            navigateToActivityWithUserKey(CommunityActivity::class.java)
        }
    }

    private fun navigateToActivityWithUserKey(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)

        // SharedPreferences에서 사용자 고유 키 값 가져오기
        val sharedPrefs = getSharedPreferences(LoginActivity.PREFS_FILENAME, Context.MODE_PRIVATE)
        val userKey = sharedPrefs.getString("USER_KEY", null)

        // Intent에 사용자 키 값 추가
        intent.putExtra("USER_KEY", userKey)

        startActivity(intent)
    }
}
