package com.example.what_s_in_my_luggage

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class StoredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stored)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val sharedPrefs = getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        val selectedButtonId = sharedPrefs.getInt("SELECTED_BUTTON_ID", R.id.btnNaviStored)

        val bottomNavFragment = BottomNavigationFragment.newInstance(selectedButtonId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottomNavigationFragment, bottomNavFragment) // XML에서 정의된 ID로 변경
            .commit()
    }
}