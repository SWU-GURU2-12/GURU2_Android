package com.example.what_s_in_my_luggage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogListViewFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_list_view, container, false)

        // 초기화
        var test = arrayListOf<ListViewItem>(
            ListViewItem("서울", "한국"),
            ListViewItem("도쿄", "일본"),
            ListViewItem("베이징", "중국"),
            ListViewItem("뉴욕", "미국"),
            ListViewItem("런던", "영국"),
            ListViewItem("파리", "프랑스"),
            ListViewItem("로마", "이탈리아"),
            ListViewItem("베를린", "독일"),
            ListViewItem("모스크바", "러시아"),
            ListViewItem("캔버라", "호주"),
            ListViewItem("오클랜드", "뉴질랜드"),
            ListViewItem("두바이", "아랍에미리트"),
            ListViewItem("카이로", "이집트"),
            ListViewItem("앙코르왓", "캄보디아"),
            ListViewItem("방콕", "태국"),
            ListViewItem("하노이", "베트남"),
            ListViewItem("마닐라", "필리핀"),
            ListViewItem("뉴델리", "인도"),
            ListViewItem("모스크바", "러시아"),
            ListViewItem("앙코르왓", "캄보디아"),
            ListViewItem("방콕", "태국"),
            ListViewItem("하노이", "베트남"),
            ListViewItem("마닐라", "필리핀"),
            ListViewItem("뉴델리", "인도"),
            ListViewItem("모스크바", "러시아"),
            ListViewItem("앙코르왓", "캄보디아"),
            ListViewItem("방콕", "태국"),
            ListViewItem("하노이", "베트남"),
            ListViewItem("마닐라", "필리핀"),
            ListViewItem("뉴델리", "인도"),
            ListViewItem("모스크바", "러시아"),
            ListViewItem("앙코르왓", "캄보디아")
        )

        val editSearch = view.findViewById<EditText>(R.id.editSearch)
        val listView = view.findViewById<ListView>(R.id.listView)

        // listView 구성
        val listAdapter = DialogListViewAdapter(test)
        listView.adapter = listAdapter

        return view
    }

}