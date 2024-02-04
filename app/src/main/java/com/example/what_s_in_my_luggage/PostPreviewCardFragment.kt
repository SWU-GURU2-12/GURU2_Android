package com.example.what_s_in_my_luggage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout

class PostPreviewCardFragment() : Fragment() {

    private lateinit var dataManager : UserDataManager

    private lateinit var context: Context
    private lateinit var luggageID : String
    private lateinit var postTitle : TextView
    private lateinit var btnBookmark : ImageButton
    private lateinit var imgRandom : ImageButton
    private lateinit var imgCarrier : ImageButton

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_preview_card, container, false)

        // 초기화
        dataManager = UserDataManager.getInstance(context)

        postTitle = view.findViewById(R.id.postTitle)
        btnBookmark = view.findViewById(R.id.btnBookmark)
        imgRandom = view.findViewById(R.id.imgRandom)
        imgCarrier = view.findViewById(R.id.imgCarrier)

        // bundle에서 데이터 받아서 setPostPreviewCard 함수 호출
        val luggageID = arguments?.getString("luggageID") ?: "luggage0"
        val postTitle = arguments?.getString("postTitle") ?: "신나는 거제도 여행"
        val isBookmarked = arguments?.getBoolean("isBookmarked") ?: true
        val imgFront = arguments?.getInt("imgFront") ?: R.drawable.front3
        val imgBack = arguments?.getInt("imgBack") ?: R.drawable.front4
        setPostPreviewCard(luggageID, postTitle, isBookmarked, imgFront, imgBack)

        // 북마크
        btnBookmark.setOnClickListener {
            if (btnBookmark.drawable.constantState == resources.getDrawable(R.drawable.ic_star_empty).constantState) {
                btnBookmark.setImageResource(R.drawable.ic_star)
                dataManager.addSavedTemplate(luggageID) // 데이터 저장
            } else {
                btnBookmark.setImageResource(R.drawable.ic_star_empty)
                dataManager.removeSavedTemplate(luggageID) // 데이터 삭제
                // stored activity -> 삭제
                if (context is StoredActivity) {
                    (context as StoredActivity).removePostPreviewCard(this)
                }
            }
            btnBookmark.scaleType = ImageView.ScaleType.FIT_XY
        }

        // 랜덤 이미지, 캐리어 이미지
        imgRandom.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity ::class.java)
            startActivity(intent)
        }
        imgCarrier.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity ::class.java)
            startActivity(intent)
        }
        return view
    }

    fun setPostPreviewCard(luggageID: String, postTitle: String, bookMark: Boolean, imgRandom: Int, imgCarrier: Int) {
        this.luggageID = luggageID
        this.postTitle.text = postTitle
        this.imgRandom.setImageResource(imgRandom)
        this.imgRandom.scaleType = ImageView.ScaleType.FIT_XY
        this.imgCarrier.setImageResource(imgCarrier)
        this.imgCarrier.scaleType = ImageView.ScaleType.FIT_XY

        if (bookMark) {
            this.btnBookmark.setImageResource(R.drawable.ic_star)
        } else {
            this.btnBookmark.setImageResource(R.drawable.ic_star_empty)
        }
        this.btnBookmark.scaleType = ImageView.ScaleType.FIT_XY
    }
}