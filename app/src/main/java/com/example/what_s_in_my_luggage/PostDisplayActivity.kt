package com.example.what_s_in_my_luggage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import com.example.what_s_in_my_luggage.model.Luggage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostDisplayActivity : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var destinationTextView: TextView
    private lateinit var scheduleTextView: TextView
    private lateinit var carrierNameTextView: TextView
    private lateinit var carrierImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_display) // 레이아웃 파일 이름 확인 및 필요 시 변경

        databaseRef = FirebaseDatabase.getInstance().getReference("posts")

        // 레이아웃에서 뷰들을 찾아 변수에 할당
        initializeViews()

        // 인텐트에서 postId 추출
        val postId = intent.getStringExtra("postId")
        if (postId != null) {
            loadPost(postId)
        } else {
            Toast.makeText(this, "Error: Post ID is missing", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeViews() {
        titleTextView = findViewById(R.id.titlepost)
        contentTextView = findViewById(R.id.contentpost)
        destinationTextView = findViewById(R.id.dtpost)
        scheduleTextView = findViewById(R.id.clpost)
        carrierNameTextView = findViewById(R.id.cnpost)
        carrierImageView = findViewById(R.id.carrierpost)
    }

    private fun loadPost(postId: String) {
        databaseRef.child(postId).get().addOnSuccessListener { dataSnapshot ->
            val post = dataSnapshot.getValue(Luggage::class.java)
            if (post != null) {
                titleTextView.text = post.title
                contentTextView.text = post.content
                destinationTextView.text = post.destination
                scheduleTextView.text = post.schedule
                carrierNameTextView.text = post.carriername
                // 이미지 URL이 있다면 Glide를 사용하여 이미지 뷰에 로드
                // itemListInLuggage 처리 로직 추가
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load post", Toast.LENGTH_SHORT).show()
        }
    }
}
