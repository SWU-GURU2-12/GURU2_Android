package com.example.what_s_in_my_luggage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.ListFragment

class PackingFrameActivity : AppCompatActivity() {
    lateinit var btnBack : Button
    lateinit var btnNext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packing_frame)

        // 초기화
        btnBack = findViewById<Button>(R.id.btnBack)
        btnNext = findViewById<Button>(R.id.btnNext)

        setFragment()
    }

    /**
     * fragment를 삽입하는 레이아웃 : packingFragment
     */
    fun setFragment() {
        // activity에 fragment를 삽입하기 위해서 framgent manager를 통해 삽입할 레이아웃의 id를 지정함
        // framgent를 삽입하는 과정은 하나의 트랜잭션으로 관리되기 때문에 transaction manager를 통해
        // begin transaction > add fragment > commit transaction 순서로 처리됨

        val addCarrierFragment = AddCarrierFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.packingFragment, addCarrierFragment)
        transaction.commit()
    }
}