package com.example.what_s_in_my_luggage

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")

    lateinit var btnBack: Button
    lateinit var etUserNickname: EditText
    lateinit var etUserPw: EditText
    lateinit var btnSignOk: Button

    companion object {
        const val PREFS_FILENAME = "com.example.what_s_in_my_luggage.prefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        etUserNickname = findViewById(R.id.userNickName)
        etUserPw = findViewById(R.id.userPw)
        btnSignOk = findViewById(R.id.btnSignOk)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnSignOk.setOnClickListener {
            val userNickName = etUserNickname.text.toString()
            val userPw = etUserPw.text.toString()

            databaseReference.orderByChild("nickname").equalTo(userNickName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (userSnapshot in dataSnapshot.children) {
                                val dbpassword = userSnapshot.child("password").getValue(String::class.java)

                                if (dbpassword == userPw) {
                                    // 비밀번호가 일치하는 경우
                                    Toast.makeText(baseContext,"로그인 완료", Toast.LENGTH_SHORT).show()
                                    // 사용자의 고유 키 값을 SharedPreferences에 저장
                                    val userKey = userSnapshot.key
                                    saveUserKey(userKey)

                                    // 로그인 성공 후 MyRoomActivity 시작
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    break
                                }
                            }
                        } else {
                            // 닉네임이 없는 경우
                            Toast.makeText(baseContext, "닉네임 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // 데이터베이스 에러 처리
                        Toast.makeText(baseContext, "데이터베이스 오류: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun saveUserKey(userKey: String?) {
        val sharedPrefs = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putString("USER_KEY", userKey)
            apply()
        }
    }
}
