import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.what_s_in_my_luggage.R
import com.google.firebase.database.*

class WritePostActivity : AppCompatActivity() {

    // Firebase 관련 변수
    lateinit var database: FirebaseDatabase
    lateinit var postReference: DatabaseReference

    // UI 관련 변수
    lateinit var etTitle: EditText
    lateinit var etContent: EditText
    lateinit var btnSavePost: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

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
                    // 글 등록이 성공하면 원하는 작업 수행
                    // 예를 들면 리스트 갱신이나 다른 동작을 추가하세요.
                } else {
                    // 글 등록이 실패하면 사용자에게 알림 또는 로깅
                }
            }
    }
}
