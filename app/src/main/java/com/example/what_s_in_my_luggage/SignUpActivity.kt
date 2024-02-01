package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SignUpActivity : AppCompatActivity() {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")

    lateinit var btnBack: Button
    lateinit var etMakeId: EditText
    lateinit var etMakePw: EditText
    lateinit var btnNicknameCd: Button
    lateinit var btnSignOk: Button
    var isNicknameAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        etMakeId = findViewById(R.id.makeId)
        etMakePw = findViewById(R.id.makePw)
        btnNicknameCd = findViewById(R.id.btnNicNameCd)
        btnSignOk = findViewById(R.id.btnSignOk)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnNicknameCd.setOnClickListener {
            val userNickName = etMakeId.text.toString()

            if (userNickName.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            databaseReference.orderByChild("nickname").equalTo(userNickName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 닉네임이 중복된 경우
                            Toast.makeText(baseContext, "이미 등록된 별명입니다", Toast.LENGTH_SHORT).show()
                            isNicknameAvailable = false
                        } else {
                            // 닉네임이 중복되지 않는 경우
                            Toast.makeText(baseContext, "사용 가능한 별명입니다", Toast.LENGTH_SHORT).show()
                            isNicknameAvailable = true
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // DB 접근 실패 시
                        Toast.makeText(baseContext, "데이터베이스 접근 오류", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        btnSignOk.setOnClickListener {
            val userNickName = etMakeId.text.toString()
            val userPw = etMakePw.text.toString()

            if (userNickName.isEmpty() || userPw.isEmpty()) {
                Toast.makeText(this, "닉네임과 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isNicknameAvailable) {
                // 중복되지 않은 닉네임인 경우, 데이터베이스에 사용자 정보 저장
                val userData = hashMapOf("nickname" to userNickName, "password" to userPw)
                databaseReference.push().setValue(userData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "회원 등록이 완료되었습니다", Toast.LENGTH_SHORT).show()
                            // HomeActivity로 이동
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(baseContext, "회원 등록에 실패했습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "닉네임 중복 확인이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
