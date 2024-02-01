package com.example.what_s_in_my_luggage

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Shared Preferences에서 마지막으로 선택된 버튼의 ID를 가져옴
        // 앱이 처음 시작될 때 기본적으로 MainActivity 버튼이 선택되도록 설정
        val sharedPrefs = getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        val selectedButtonId = sharedPrefs.getInt("SELECTED_BUTTON_ID", R.id.btnNaviMain)

        // 바텀 네비게이션 프래그먼트 생성 및 선택된 버튼 ID 전달
        val bottomNavFragment = BottomNavigationFragment.newInstance(selectedButtonId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavigationFragment, bottomNavFragment) // 'bottomNavigationFragment'는 프래그먼트를 배치할 레이아웃의 ID입니다.
            .commit()
    }
}
