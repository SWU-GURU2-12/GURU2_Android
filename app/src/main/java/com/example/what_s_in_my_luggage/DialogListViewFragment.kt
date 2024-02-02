package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResult
import com.example.what_s_in_my_luggage.model.ListViewItem
import com.example.what_s_in_my_luggage.model.SavedTemplate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        // 데이터 가져오기
        var test = arrayListOf<ListViewItem>()
        val tag = arguments?.getString("tag")
        var dataManager = UserDataManager.getInstance(requireContext())

        if (tag == "travelPlace") { // travel place
            test = dataManager.getTravelPlaceList()
            setUpListView(test)
            setUpSearchView(test)
        } else if (tag == "template") { // template
            test = dataManager.getSavedTemplateListView()
            setUpListView(test)
            setUpSearchView(test)
        }


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