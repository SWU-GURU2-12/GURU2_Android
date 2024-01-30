package com.example.what_s_in_my_luggage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.what_s_in_my_luggage.AddCarrierFragment

class PackingFrameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packing_frame)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddCarrierFragment.newInstance())
                .commitNow()
        }
    }
}