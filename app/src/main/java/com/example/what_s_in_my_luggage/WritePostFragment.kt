package com.example.what_s_in_my_luggage

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.example.what_s_in_my_luggage.R.*
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.what_s_in_my_luggage.model.Luggage
import com.google.firebase.database.FirebaseDatabase

class WritePostFragment : Fragment() {
    var packingFrameActivity: PackingFrameActivity? = null

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private val databaseRef = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_write_post, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etContent = view.findViewById(R.id.etContent)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is PackingFrameActivity) {
            packingFrameActivity = context
        }
    }

    fun savePostToFirebase() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()
        // 여기서 추가 정보를 받아오는 로직 필요
        val destination = "" // AddCarrierFragment에서 받아온 정보
        val schedule = "" // AddCarrierFragment에서 받아온 정보
        val carriername = "" // AddCarrierFragment에서 받아온 정보
        val luggageID = ""
        val userName = ""

        if (title.isNotEmpty()) {
            val postId = databaseRef.push().key
            val luggage = Luggage(title, content, destination, schedule, carriername, luggageID, userName)

            postId?.let {
                databaseRef.child("posts").child(it).setValue(luggage)
                    .addOnCompleteListener {
                        Toast.makeText(context, "글이 발행되었습니다.", Toast.LENGTH_SHORT).show()
                        // 성공시 처리 로직, 예를 들어 Fragment를 종료하거나 다른 화면으로 이동
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "글 발행에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        // 실패시 처리 로직
                    }
            }
        } else {
            Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}