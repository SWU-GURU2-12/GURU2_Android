package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import com.bumptech.glide.Glide
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
    private lateinit var imageURL: ImageView

    // check list
    private lateinit var electronics : TextView
    private lateinit var otherCloth : TextView
    private lateinit var inflight : TextView
    private lateinit var care : TextView
    private lateinit var cloths : TextView
    private lateinit var food : TextView

    lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_display) // 레이아웃 파일 이름 확인 및 필요 시 변경

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, MyRoomActivity::class.java)
            startActivity(intent)
        }

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

        // check list
        setCheckList()
    }

    private fun initializeViews() {
        titleTextView = findViewById(R.id.titlepost)
        contentTextView = findViewById(R.id.contentpost)
        destinationTextView = findViewById(R.id.dtpost)
        scheduleTextView = findViewById(R.id.clpost)
        carrierNameTextView = findViewById(R.id.cnpost)
        imageURL = findViewById(R.id.carrierpost)

        // check list
        electronics = findViewById(R.id.electronics)
        otherCloth = findViewById(R.id.otherCloth)
        inflight = findViewById(R.id.inflight)
        care = findViewById(R.id.care)
        cloths = findViewById(R.id.cloths)
        food = findViewById(R.id.food)
    }

    private fun setCheckList() {
        electronics.text = ""
        otherCloth.text = ""
        inflight.text = ""
        care.text = ""
        cloths.text = ""
        food.text = ""

        var dataManager = UserDataManager.getInstance(this)
        var checklist = dataManager.tempLuggage?.itemListInLuggage

        for (item in checklist!!) {
            when (item) {
                "어댑터", "카메라", "보조배터리" -> {
                    electronics.text = electronics.text.toString() + item + "\n"
                }
                "여권", "개인 가방", "유럽 돈" -> {
                    inflight.text = inflight.text.toString() + item + "\n"
                }
                "겨울 상의", "겨울 하의" -> {
                    cloths.text = cloths.text.toString() + item + "\n"
                }
                "머플러", "모자", "부츠" -> {
                    otherCloth.text = otherCloth.text.toString() + item + "\n"
                }
                "화장품", "칫솔&치약", "스킨케어" -> {
                    care.text = care.text.toString() + item + "\n"
                }
                "컵라면" -> {
                    food.text = food.text.toString() + item + "\n"
                }
            }
        }
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
                post.imageURL?.let { url ->
                    Glide.with(this@PostDisplayActivity).load(url).into(imageURL)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load post", Toast.LENGTH_SHORT).show()
        }
    }
}
