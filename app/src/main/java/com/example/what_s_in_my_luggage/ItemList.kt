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
//    private var clickedImageView: ImageView? = null
//    val CONTEXT_MENU_REMOVE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iBinding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(iBinding.root)

//        // 이전에 저장한 ImageView 위치 복원
//        if (savedInstanceState != null) {
//            val itemX = savedInstanceState.getFloat("itemX")
//            val itemY = savedInstanceState.getFloat("itemY")
//            restoreImageViewPosition(itemX, itemY)
//        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        // ImageView의 현재 위치를 저장
//        val imageView = findViewById<ImageView>(R.id.your_image_view_id)
//        outState.putFloat("itemX", imageView.x)
//        outState.putFloat("itemY", imageView.y)
//    }
//
//    private fun restoreImageViewPosition(itemX: Float, itemY: Float) {
//        // 저장한 위치로 ImageView 위치 복원
//        val imageView = findViewById<ImageView>(R.id.your_image_view_id)
//        val layoutParams = imageView.layoutParams as ConstraintLayout.LayoutParams
//        layoutParams.leftMargin = itemX.toInt()
//        layoutParams.topMargin = itemY.toInt()
//        imageView.layoutParams = layoutParams
//    }

    companion object {
        private var offsetX = 0
        private var offsetY = 0
        private var isMoving = false
        var isItemExist = false
        val existTextColor = R.color.blue
        val notExistTextColor = R.color.bb25
        var isItemsLoaded = false
        private var clickedItem: Items? = null
//        val CONTEXT_MENU_REMOVE = 1

        fun onImageViewClick(v: View, clickedItem: Items) {
            if (v is ImageView) {
                val clickedDrawable = v.drawable

                val newImageView = ImageView(v.context)
                newImageView.setImageDrawable(clickedDrawable)

                // 새로 추가될 ImageView의 id 설정
                newImageView.id = View.generateViewId()
//                newImageView.x = 170.0f
//                newImageView.y = 183.0f

                // ImageView의 넓이, 높이, 가운데 정렬 등 옵션 추가
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // 화면 크기와 ImageView 크기를 고려하여 초기 위치 설정
//                    val initialLeftMargin = -100
//                    val initialTopMargin = -100
                    setMargins(0, 0, 0, 0)
                    startToStart = ConstraintSet.PARENT_ID
                    topToTop = ConstraintSet.PARENT_ID
                }

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
//                    this.clickedItem = clickedItem
                    true
                }

//                newImageView.setOnLongClickListener {
//                    openContextMenu(imageView)
//                    true
//                }
            }
        }

        fun handleTouch(
            event: MotionEvent,
            view: View,
            layout: ConstraintLayout,
            item: Items
        ): Boolean {
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
            val imageView = view as ImageView // view를 ImageView로 캐스팅

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
//                        val newLeft = (event.rawX.toInt() - offsetX).coerceIn(0, layout.width - view.width)
//                        val newTop = (event.rawY.toInt() - offsetY).coerceIn(0, layout.height - view.height)
//                        Log.d("layout","layout.width : ${layout.width}, layout.height : ${layout.height}")

                        layoutParams.leftMargin = newLeft
                        layoutParams.topMargin = newTop

                        view.layoutParams = layoutParams
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // 터치 업 시의  처리
                    isMoving = false

                    // Firebase 데이터베이스의 참조 생성
                    val itemRef = FirebaseDatabase.getInstance().getReference("checklist")
                        .child("seoyoung").child("luggage1")

                    // 업데이트할 그룹 이름을 찾음
//                    itemRef.orderByChild("itemName").equalTo(item.name.toString())
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                for (snapshot in dataSnapshot.children) {
//                                    snapshot.ref.child("itemX").setValue(imageView.x)
//                                    snapshot.ref.child("itemY").setValue(imageView.y)
//                                }
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {
//                                Log.e("dataChange_cancelled", "Error: ${databaseError.message}")
//                            }
//                        })

                    // 좌표 업데이트
//                    itemRef.child("itemX").setValue(newX)
//                    itemRef.child("itemY").setValue(newY)


//                    // Firebase에 좌표 업데이트
//                    val itemRef = FirebaseDatabase.getInstance().getReference("checklist")
//                        .child("seoyoung").child("luggage1").child(item.name)

//                    itemRef.runTransaction(object : Transaction.Handler {
//                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
//                            // 여기서 mutableData를 사용하여 필요한 데이터 업데이트
//                            mutableData.child("itemX").value = view.x
//                            mutableData.child("itemY").value = view.y
//                            return Transaction.success(mutableData) // Transaction을 성공으로 마무리
//                        }
//
//                        override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
//                            // Transaction 완료 후 처리
//                            Log.d("Database Transaction", "postTransaction:onComplete:$databaseError")
//                        }
//                    })

//                    // Firebase에 좌표 업데이트
//                    item.x = view.x
//                    item.y = view.y
//
//                    // Firebase 데이터베이스의 참조 생성
//                    val databaseRef =
//                        FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung")
//                            .child("luggage1")
//
//                    // 업데이트할 그룹 이름을 찾음
//                    databaseRef.orderByChild("itemName").equalTo(item.name.toString())
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                for (snapshot in dataSnapshot.children) {
//                                    snapshot.ref.child("itemX").setValue(item.x)
//                                    snapshot.ref.child("itemY").setValue(item.y)
//                                }
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {
//                                Log.e("dataChange_cancelled", "Error: ${databaseError.message}")
//                            }
//                        })

//                    val valueEventListener = object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            for (snapshot in dataSnapshot.children) {
//                                if (snapshot.child("itemName")
//                                        .getValue(String::class.java) == item.name
//                                ) {
//                                    // 좌표 업데이트
//                                    snapshot.ref.child("itemX").setValue(item.x)
//                                    snapshot.ref.child("itemY").setValue(item.y)
//                                    break
//                                }
//                            }
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                            Log.e("dataChange_cancelled", "Error: ${databaseError.message}")
//                        }
//                    }
                }
            }
            return true
        }
    }
}

//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        menu?.setHeaderTitle("아이템 동작")
//        menu?.add(0, CONTEXT_MENU_REMOVE, 0, "아이템 제거")
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            CONTEXT_MENU_REMOVE -> {
//                // 아이템을 제거하는 코드
//                if (isMoving && isItemExist) {
//                    val layout = findViewById<ConstraintLayout>(R.id.luggageLayout)
//                    clickedImageView?.let {
//                        layout.removeView(it)
//                        isItemExist = true
//                        // Firebase에서도 제거
//                        clickedItem?.let { it1 -> removeItemFromFirebase(it1) }
//                    }
//                }
//                return true
//            }
//        }
//        return super.onContextItemSelected(item)
//    }
//
//    fun removeItemFromFirebase(item: Items) {
//        val databaseRef =
//            FirebaseDatabase.getInstance().getReference("checklist").child("seoyoung")
//                .child("luggage1")
//        databaseRef.orderByChild("itemName").equalTo(item.name.toString())
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    for (snapshot in dataSnapshot.children) {
//                        snapshot.ref.removeValue()
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.e("dataChange_cancelled", "Error: ${databaseError.message}")
//                }
//            })
//    }
