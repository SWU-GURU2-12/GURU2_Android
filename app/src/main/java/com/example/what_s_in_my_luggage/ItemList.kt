package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding

class ItemList : AppCompatActivity() {
    lateinit var iBinding: ActivityItemListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iBinding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(iBinding.root)
    }

    companion object {
        fun onImageViewClick(v: View) {
            if (v is ImageView) {
                val clickedDrawable = v.drawable

                val newImageView = ImageView(v.context)
                newImageView.setImageDrawable(clickedDrawable)

                // 새로 추가될 ImageView의 id 설정
                newImageView.id = View.generateViewId()

                // 새로 추가될 ImageView의 넓이, 높이, 가운데 정렬 등 옵션 추가
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )

                newImageView.layoutParams = layoutParams
                layoutParams.width = 300
                layoutParams.height = 300
                layoutParams.startToStart = ConstraintSet.PARENT_ID
                layoutParams.endToEnd = ConstraintSet.PARENT_ID
                layoutParams.topToTop = ConstraintSet.PARENT_ID
                layoutParams.bottomToBottom = ConstraintSet.PARENT_ID

                // 새로 추가될 ImageView가 luggageLayout에 추가되도록 레이아웃 지정
                val layout = v.rootView.findViewById<ConstraintLayout>(R.id.luggageLayout)

                // ImageView 추가
                layout.addView(newImageView)
            }
        }
    }
}