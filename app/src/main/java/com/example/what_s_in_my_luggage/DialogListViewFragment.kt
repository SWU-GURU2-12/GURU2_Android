package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogListViewFragment : BottomSheetDialogFragment() {
    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    var selected: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_list_view, container, false)

        // 초기화
        searchView = view.findViewById<SearchView>(R.id.searchView)
        listView = view.findViewById<ListView>(R.id.listView)
        var test = arrayListOf<ListViewItem>(
            ListViewItem("seoul", "korea"),
            ListViewItem("tokyo", "japan"),
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
            ListViewItem("busan", "korea")
        )

        setUpListView(test)
        setUpSearchView(test)

        return view
    }

    fun setUpListView(test: ArrayList<ListViewItem>) {
        // list view 구성
        val listAdapter = DialogListViewAdapter(test)
        listView.adapter = listAdapter

        // item을 클릭햇을 때
        listView.setOnItemClickListener { parent, view, position, id ->
            val item = test[position]
            selected = "${item.title}, ${item.subTitle}"
            dismiss()
        }
    }

    fun setUpSearchView(test: ArrayList<ListViewItem>) {
        // search view 구성
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼이 눌렸을 때 호출 되는 매소드
                // 입력된 검색어(query)를 가지고 검색을 실행
                // 검색 결과를 화면에 표시
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어가 변결될 때 호출되는 매소드
                // 입력된 검색어(newText)를 가지고 실시간 검색 기능 구현
                var filterList = ArrayList<ListViewItem>()
                if (newText != null) {
                    for (item in test) {
                        if (item.title.contains(newText) || item.subTitle.contains(newText)) {
                            filterList.add(item)
                        }
                    }
                }
                setUpListView(filterList)
                return false
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setFragmentResult("selectPlace", Bundle().apply {
            putString("place", selected)
        })
    }
}