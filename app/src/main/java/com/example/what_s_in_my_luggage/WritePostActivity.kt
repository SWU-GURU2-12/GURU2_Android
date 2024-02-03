package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.example.what_s_in_my_luggage.R
import com.google.firebase.Firebase
import com.google.firebase.database.*

class WritePostActivity : AppCompatActivity() {

    // Firebase 관련 변수
    private lateinit var database: FirebaseDatabase
    private lateinit var postReference: DatabaseReference

    // UI 관련 변수
    lateinit var etTitle: EditText
    lateinit var etContent: EditText
    lateinit var btnSavePost: Button

    private fun clearForm() {
        etTitle.text.clear()
        etContent.text.clear()
        // 기타 필요한 경우 UI를 초기화하세요.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        // Firebase 초기화
        database = FirebaseDatabase.getInstance()
        // "posts"는 데이터베이스의 경로이며, 필요에 따라 수정하세요.
        postReference = database.reference.child("posts")

        // UI 요소 초기화
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSavePost = findViewById(R.id.btnSavePost)

        btnSavePost.setOnClickListener {
            savePostToFirebase()
        }
    }

    fun savePostToFirebase() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        // 제목이 비어있는지 확인
        if (title.isEmpty()) {
            etTitle.error = "제목을 입력하세요."
            return
        }

        // 글 작성 데이터 생성
        val post = HashMap<String, Any>()
        post["title"] = title
        post["content"] = content

        // Firebase에 데이터 추가
        val postId = postReference.push().key // 새로운 글을 위한 고유한 키 생성
        postReference.child(postId!!).setValue(post)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 글 등록이 성공하면 다른 글의 데이터를 가져와서 현재 글에 추가
                    fetchOtherPostDataAndMerge(postId)
                    clearForm() // 입력 폼 초기화
                } else {
                    // 글 등록이 실패하면 사용자에게 알림 또는 로깅
                    showFailurePopup()
                }
            }
    }

    private fun fetchOtherPostDataAndMerge(postId: String) {
        // 다른 글의 키를 사용하여 데이터를 가져옴
        val otherPostReference = postReference.child("otherPost")

        otherPostReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 다른 글의 데이터를 가져옴 (DB 정리 후 추합 필요)
                    val otherPostList = dataSnapshot.child("list").getValue(String::class.java)
                    val otherPostCarrier = dataSnapshot.child("carrier").getValue(String::class.java)

                    // 현재 글에 다른 글의 데이터를 추가 또는 병합
                    mergeOtherPostData(postId, otherPostList, otherPostCarrier)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database read error: ${databaseError.message}")
            }
        })
    }

    private fun mergeOtherPostData(postId: String, otherPostList: String?, otherPostCarrier: String?) {
        // 현재 글에 다른 글의 데이터를 추가 또는 병합하는 로직을 작성
        // 현재 글의 데이터에 다른 글의 데이터를 더하는 경우
        val currentPostList = "현재 글의 데이터" // 현재 글의 데이터를 가져와서 저장
        val currentPostCarrier = "현재 글의 데이터" // 현재 글의 데이터를 가져와서 저장

        // 다른 글의 데이터를 현재 글의 데이터에 추가 또는 병합
        val mergedList = currentPostList + (otherPostList ?: "")
        val mergedCarrier = currentPostCarrier + (otherPostCarrier ?: "")

        // 그리고 나서 데이터를 저장하거나 활용하는 등의 작업을 수행
    }
    private fun showFailurePopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("글 등록 실패")
            .setMessage("글을 등록하는 중에 오류가 발생했습니다.")
            .setPositiveButton("확인") { _, _ ->
                // 팝업 확인 버튼을 눌렀을 때 실행할 동작 추가 (예: 다시 시도)
                savePostToFirebase()
            }
            .setCancelable(false) // 사용자가 팝업 외부를 터치하면 닫히지 않도록 설정
            .show()
    }
}