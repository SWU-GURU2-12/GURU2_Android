package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogCalFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog_cal, container, false)

        val txtDate = view.findViewById<TextView>(R.id.date)
        val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        val buttonId = arguments?.getInt("button")

        calendar?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var selectedDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            txtDate?.text = "출발일: ${selectedDate}"

            setFragmentResult("selectDate", Bundle().apply {
                putString("date", selectedDate)
                putInt("button", buttonId!!)
            })
        }

        return view
    }

}