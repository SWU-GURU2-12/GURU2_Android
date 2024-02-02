package com.example.what_s_in_my_luggage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.what_s_in_my_luggage.model.ListViewItem

class DialogListViewAdapter(private val items: MutableList<ListViewItem>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ListViewItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    // xml 파일의 view와 데이터를 연결
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // 리스트 1칸을 구성하는 뷰
        var view: View = LayoutInflater.from(parent?.context).inflate(R.layout.layout_dialog_list_item, null)

        // list_item.xml 과 뷰를 연결
        val title = view.findViewById<TextView>(R.id.title)
        val subTitle = view.findViewById<TextView>(R.id.subTitle)

        // 데이터 연결
        val item = items[position]
        title.text = item.title
        subTitle.text = item.subTitle
        
        return view
    }
}