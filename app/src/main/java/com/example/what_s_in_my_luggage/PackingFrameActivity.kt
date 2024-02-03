package com.example.what_s_in_my_luggage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.what_s_in_my_luggage.model.Luggage

class PackingFrameActivity : AppCompatActivity() {

    var currentLuggage: Luggage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packing_frame)

        // 초기화
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val pageTitle = findViewById<TextView>(R.id.pageTitle)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // 4. 짐꾸리기 화면 (PackingFrameActivity)
        val fragments: Array<Fragment> = arrayOf(
            AddCarrierFragment(), // 캐리어 추가
            TestBlankFragment() // 짐 꾸리기
            // 짐 꾸리기 리스트
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
                        // add carrier fragment에서 선택한 캐리어 정보를 저장
                        // TODO: 다른 방법?
                        val addCarrierFragment = fragments[currentFragment] as AddCarrierFragment
                        currentLuggage = addCarrierFragment.getLuggage()

                        // TODO: test bundel
                        val bundle = Bundle()
                        bundle.putString("userName", currentLuggage?.userName)
                        bundle.putString("title", "b")
                        bundle.putString("destination", "c")
                        bundle.putString("schedule", "d")

                        fragments[currentFragment + 1].arguments = bundle
                    }
                    1 -> { // 짐 꾸리기 -> 짐 꾸리기 리스트

                    }
                    2 -> { // 짐 꾸리기 리스트 -> 템플릿 발행하기
                        btnNext.text = "발행"
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
}