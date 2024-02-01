package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.util.Log
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
        var itemX = 0f
        var itemY = 0f

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
                )

                newImageView.layoutParams = layoutParams
                layoutParams.width = 100
                layoutParams.height = 100
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
                    handleTouch(event, newImageView, layout, clickedItem)
//                    if (event.action == MotionEvent.ACTION_UP) {
//                        updateItemCoordinates(newImageView, clickedItem)
//                    }
                    true
//                    val isTouchHandled = event.action == MotionEvent.ACTION_UP
//                    if (isTouchHandled) {
//                        updateItemCoordinates(newImageView, clickedItem)
//                    }
//                    isTouchHandled
                }
            }
        }

        fun handleTouch(event: MotionEvent, view: View, layout: ConstraintLayout, item: Items): Boolean {
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
                        val newLeft = event.rawX.toInt() - offsetX
                        val newTop = event.rawY.toInt() - offsetY

                        // ImageView가 레이아웃의 경계를 벗어나지 않도록 위치 조정
                        val leftMargin = newLeft.coerceIn(view.width - layout.width, layout.width - view.width)
                        val topMargin = newTop.coerceIn(view.height - layout.height, layout.height - view.height)

                        layoutParams.leftMargin = leftMargin
                        layoutParams.topMargin = topMargin

                        view.layoutParams = layoutParams
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // 터치 업 시의 처리
                    isMoving = false
                    item.x = view.x
                    item.y = view.y

                    //Firebase 데이터베이스의 참조 생성
                    val databaseRef = FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung").child("luggage1")

                    //업데이트할 그룹 이름을 찾음
                    databaseRef.orderByChild("itemName").equalTo(item.name.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                snapshot.ref.child("itemX").setValue(item.x.toString().toFloat())
                                snapshot.ref.child("itemY").setValue(item.y.toString().toFloat())
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("dataChange_cancelled", "Error: ${databaseError.message}")
                        }
                    })
                }
            }
            return true
        }
    }
}