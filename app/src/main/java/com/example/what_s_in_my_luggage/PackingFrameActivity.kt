package com.example.what_s_in_my_luggage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.what_s_in_my_luggage.model.Luggage

class PackingFrameActivity : AppCompatActivity() {

//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packing_frame)

        // 초기화
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val pageTitle = findViewById<TextView>(R.id.pageTitle)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

//        val fragment = supportFragmentManager.findFragmentById(R.id.packingFragment) as? PackLuggageFragment
//        val luggageLayout = fragment?.view?.findViewById<ConstraintLayout>(R.id.luggageLayout)

        // 4. 짐꾸리기 화면 (PackingFrameActivity)
        val fragments: Array<Fragment> = arrayOf(
            AddCarrierFragment(), // 캐리어 추가
            PackLuggageFragment(), // 짐 꾸리기
            CheckListFragment()
            // 템플릿 발행하기
        )
        var currentFragment = 0
        val titleList: Array<String> = arrayOf(
            "짐 꾸리기",
            "짐 꾸리기",
            "짐 꾸리기 리스트",
            "템플릿 발행하기"
        )

        // 버튼
        btnNext.setOnClickListener() {
            if (currentFragment < fragments.size - 1) {
                when (currentFragment) {
                    0 -> { // 캐리어 추가 -> 짐 꾸리기
                        val fragment = fragments[currentFragment] as AddCarrierFragment
                        fragment.saveTempLuggage()
                    }
                    1 -> { // 짐 꾸리기 -> 짐 꾸리기 리스트
                        val fragment = fragments[currentFragment] as PackLuggageFragment
                        val luggageLayout = fragment?.view?.findViewById<ConstraintLayout>(R.id.luggageLayout)
                        if (luggageLayout != null) {
                            requestCapture(luggageLayout)
                        }
                    }
                    2 -> { // 짐 꾸리기 리스트 -> 템플릿 발행하기
                        btnNext.text = "발행"
//
                    }
                }
                currentFragment++
                replaceFragment(fragments[currentFragment])
                pageTitle.text = titleList[currentFragment]
                progressBar.progress = (currentFragment + 1) * (100/fragments.size)
            }
            else { // 발행하기 activity로 이동

            }
        }

        btnBack.setOnClickListener() {
            if (currentFragment > 0) {
                when (currentFragment) {
                    3 -> { // 템플릿 발행하기 -> 짐 꾸미기 리스트
                        btnNext.text = "다음"
                    }
                    2 -> { // 짐 꾸미기 리스트 -> 짐 꾸미기
                        UserDataManager.getInstance(this).tempLuggage?.currentTime?.let { it1 ->
                            Log.d("removeremove_1","ok")
                            Log.d("removeremove_tempLuggage information_1", "${UserDataManager.getInstance(this).tempLuggage?.currentTime}")
                            UserDataManager.getInstance(this).removeScreenshotFromFirebase(
                                it1
                            )
                            Log.d("removeremove_2","ok")
                            Log.d("removeremove_tempLuggage information_2", "${UserDataManager.getInstance(this).tempLuggage?.currentTime}")
                        }
                        UserDataManager.getInstance(this).removeItemListInLuggage()
//                        UserDataManager.getInstance(this).removeCurrentTime()
//                        UserDataManager.getInstance(this).removeItemListInLuggage()
                    }
                    1 -> { // 짐 꾸리기 -> 캐리어 추가

                    }
                    0 -> { // 캐리어 추가 -> main activity로 이동
                        // main activity로 이동
                        // 다른 액티비티로 전환
                        finish()
                        val intent = Intent(this, MainActivity ::class.java)
                        startActivity(intent)
                    }
                }
                currentFragment--
                supportFragmentManager.popBackStack()
                pageTitle.text = titleList[currentFragment]
                progressBar.progress = (currentFragment + 1) * (100/fragments.size)
            }
        }

        // first fragment
        replaceFragment(fragments[currentFragment])
        pageTitle.text = titleList[currentFragment]
        progressBar.progress = (currentFragment + 1) * (100/fragments.size)
    }

    fun replaceFragment(fragment: Fragment) {
        // activity에 fragment를 삽입하기 위해서 framgent manager를 통해 삽입할 레이아웃의 id를 지정함
        // framgent를 삽입하는 과정은 하나의 트랜잭션으로 관리되기 때문에 transaction manager를 통해
        // begin transaction > add fragment > commit transaction 순서로 처리됨
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.packingFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun requestCapture(layout: ConstraintLayout) {
        if (layout == null) {
            println("::::ERROR:::: captureTargetLayout == NULL")
            return
        }

        // 캐시 비트맵 만들기
        layout.buildDrawingCache()
        val bitmap: Bitmap = layout.drawingCache

        // Firebase Storage에 이미지 업로드
        UserDataManager.getInstance(this).setCurrentTime()
        var time = UserDataManager.getInstance(this).tempLuggage?.currentTime
        var fileName = "$time\\_capture"
        var dataManager = UserDataManager.getInstance(this)
        dataManager.uploadImageToFirebaseStorage(bitmap, fileName)
    }
}