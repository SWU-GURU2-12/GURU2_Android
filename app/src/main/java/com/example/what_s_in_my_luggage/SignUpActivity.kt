package com.example.what_s_in_my_luggage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.what_s_in_my_luggage.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SignUpActivity : AppCompatActivity() {
    lateinit var etMakeId: EditText
    lateinit var etMakePw: EditText
    lateinit var btnNicknameCd: Button
    lateinit var btnPwCd: Button
    lateinit var btnSignOk: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // 위젯 참조
        etMakeId = findViewById(R.id.makeId)
        etMakePw = findViewById(R.id.makePw)
        btnNicknameCd = findViewById(R.id.btnNicNameCd)
        btnPwCd = findViewById(R.id.btnPwCd)
        btnSignOk = findViewById(R.id.btnSignOk)

        btnNicknameCd.setOnClickListener {
            val nickname = etMakeId.text.toString()

            if (nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase 데이터베이스 참조
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.orderByChild("nickname").equalTo(nickname)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 닉네임이 중복된 경우
                            Toast.makeText(baseContext, "사용 불가한 별명입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 닉네임이 중복되지 않는 경우
                            Toast.makeText(baseContext, "사용 가능한 별명입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // DB 접근 실패 시
                        Toast.makeText(baseContext, "데이터베이스 접근 오류", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        btnPwCd.setOnClickListener {
            val username = etMakePw.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "사용자명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase 데이터베이스 참조
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // 사용자명이 중복된 경우
                            Toast.makeText(baseContext, "이미 사용 중인 사용자명입니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 사용자명이 중복되지 않는 경우
                            Toast.makeText(baseContext, "사용 가능한 사용자명입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // DB 접근 실패 시
                        Toast.makeText(baseContext, "데이터베이스 접근 오류", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        btnSignOk.setOnClickListener {

        }
    }
}




