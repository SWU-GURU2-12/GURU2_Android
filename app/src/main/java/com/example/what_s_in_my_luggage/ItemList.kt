package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.what_s_in_my_luggage.databinding.ActivityItemListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class ItemList : AppCompatActivity() {
    lateinit var iBinding: ActivityItemListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iBinding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(iBinding.root)
    }

    companion object {
        private var offsetX = 0
        private var offsetY = 0
        private var isMoving = false
        var isItemExist = false
        val existTextColor = R.color.blue
        val notExistTextColor = R.color.bb25

//        var isItemsLoaded = false

        fun onImageViewClick(v: View, clickedItem: Items) {
            if (v is ImageView) {
                val clickedDrawable = v.drawable

                val newImageView = ImageView(v.context)
                newImageView.setImageDrawable(clickedDrawable)

                // 새로 추가될 ImageView의 id 설정
                newImageView.id = View.generateViewId()

                // ImageView의 넓이, 높이, 가운데 정렬 등 옵션 추가
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 0)
                    startToStart = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }

                newImageView.layoutParams = layoutParams
                layoutParams.width = 300
                layoutParams.height = 300
                layoutParams.startToStart = ConstraintSet.PARENT_ID
                layoutParams.endToEnd = ConstraintSet.PARENT_ID
                layoutParams.topToTop = ConstraintSet.PARENT_ID
                layoutParams.bottomToBottom = ConstraintSet.PARENT_ID


                // ImageView가 luggageLayout에 추가되도록 레이아웃 지정
                val layout = v.rootView.findViewById<ConstraintLayout>(R.id.luggageLayout)
                val nextBtn = v.rootView.findViewById<Button>(R.id.nextBtn)

                // ImageView 추가
                layout.addView(newImageView)
                isItemExist = true
                nextBtn.setTextColor(v.resources.getColor(existTextColor))

                // ImageView에 터치 이벤트 리스너 등록
                newImageView.setOnTouchListener { v, event ->
                    handleTouch(event, newImageView, layout,
//                        clickedItem
                    )
                    true
                }
            }
        }

        fun handleTouch(
            event: MotionEvent,
            view: View,
            layout: ConstraintLayout
        ): Boolean {
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 다운 시의 처리
                    offsetX = event.rawX.toInt() - layoutParams.leftMargin
                    offsetY = event.rawY.toInt() - layoutParams.topMargin
                    isMoving = true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isMoving) {
                        // 터치 중일 때 같이 이동하도록 설정
                        val newLeft = (event.rawX.toInt() - offsetX).coerceIn(view.width - layout.width, layout.width - view.width)
                        val newTop = (event.rawY.toInt() - offsetY).coerceIn(view.height - layout.height, layout.height - view.height)

                        layoutParams.leftMargin = newLeft
                        layoutParams.topMargin = newTop

                        view.layoutParams = layoutParams
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // 터치 업 시의  처리
                    isMoving = false
                }
            }

            return true
        }
    }
}