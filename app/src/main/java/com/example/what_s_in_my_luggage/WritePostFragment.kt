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
    private var packingFrameActivity: PackingFrameActivity? = null

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private val databaseRef = FirebaseDatabase.getInstance().reference.child("posts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_write_post, container, false)

        etTitle = view.findViewById(R.id.etTitle)
        etContent = view.findViewById(R.id.etContent)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PackingFrameActivity) {
            packingFrameActivity = context
        }
    }

    fun savePostToFirebase() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        // UserDataManager에서 임시 데이터 가져오기
        val tempLuggage = UserDataManager.getInstance(requireContext()).tempLuggage
        val destination = tempLuggage?.destination ?: ""
        val schedule = tempLuggage?.schedule ?: ""
        val carrierName = tempLuggage?.carriername ?: ""
        val userName = UserDataManager.getInstance(requireContext()).getUserName()
        val itemList = tempLuggage?.itemListInLuggage ?: mutableListOf()
        val luggageId = tempLuggage?.luggageID ?: ""
        val imageURL = tempLuggage?.imageURL ?: ""

        if (title.isNotEmpty()) {
            // postId를 직접 지정하거나 생성

            val luggage = Luggage(luggageId, userName, carrierName, destination, schedule, title, content, imageURL).apply {
                // itemListInLuggage가 List<String> 타입
                this.itemListInLuggage = itemList
            }

            databaseRef.child(luggageId).setValue(luggage).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "글이 발행되었습니다.", Toast.LENGTH_SHORT).show()
                    (activity as? PackingFrameActivity)?.onPostPublished(luggageId)
                } else {
                    Toast.makeText(context, "글 발행에 실패했습니다: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
