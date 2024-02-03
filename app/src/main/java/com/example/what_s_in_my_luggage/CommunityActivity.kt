package com.example.what_s_in_my_luggage

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // 바텀 네비게이션 설정
        setupBottomNavigation()

        val coPostlayout = findViewById<ConstraintLayout>(R.id.coPostlayout)

        // 첫 번째 글 클릭시 발행한 글로 넘어가기
        coPostlayout.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java).apply {
                val userKey =
                    getSharedPreferences(AppConstants.PREFS_FILENAME, Context.MODE_PRIVATE)
                        .getString("USER_KEY", "")
                putExtra("USER_KEY", userKey)
            }
            startActivity(intent)
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
}
