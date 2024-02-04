package com.example.what_s_in_my_luggage

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class StoredActivity : AppCompatActivity() {
    lateinit var linearLayout: LinearLayout

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

        // 초기화
        linearLayout = findViewById(R.id.linearLayout)

        // 기본 post preview card 추가 (테스트용)
        addPostPreviewCard("luggage1", "제주도 한 달 살기", true, R.drawable.front3, R.drawable.front4)
        addPostPreviewCard("luggage2", "여자끼리 유럽 9박 10일 다녀오기", true, R.drawable.front3, R.drawable.front4)

        // TODO: saved template list를 추가
    }

    fun addPostPreviewCard(luggageID: String, postTitle: String, bookmark: Boolean, img1: Int, img2: Int) : PostPreviewCardFragment {
        val postPreviewCard = PostPreviewCardFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.linearLayout, postPreviewCard)
        }.commit()

        // 데이터 전달
        postPreviewCard.arguments = Bundle().apply {
            putString("luggageID", luggageID)
            putString("postTitle", postTitle)
            putBoolean("isBookmarked", bookmark)
            putInt("imgFront", img1)
            putInt("imgBack", img2)
        }
        return postPreviewCard
    }

    fun removePostPreviewCard(postPreviewCard: PostPreviewCardFragment) {
        var dataManager = UserDataManager.getInstance(this)
        dataManager.removeSavedTemplate(postPreviewCard.arguments?.getString("luggageID") ?: "luggage0")
        supportFragmentManager.beginTransaction().apply {
            remove(postPreviewCard)
        }.commit()
    }
}