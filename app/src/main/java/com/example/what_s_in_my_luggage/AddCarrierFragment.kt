package com.example.what_s_in_my_luggage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView

class AddCarrierFragment : Fragment() {
    private lateinit var btnDepartureCal: Button
    private lateinit var btnArrivalCal: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_add_carrier.xml과 연결하여 return 함
        var view = inflater.inflate(R.layout.fragment_add_carrier, container, false)
        
        // 초기화
        btnDepartureCal = view.findViewById<Button>(R.id.btnDepartureCal)
        btnArrivalCal = view.findViewById<Button>(R.id.btnArrivalCal)
        var travelPlace = view.findViewById<CardView>(R.id.travelPlace)
        var template = view.findViewById<CardView>(R.id.template)

        // 가는 날, 오는 날 -> 달력 다이얼로그 띄우기
        btnDepartureCal.setOnClickListener {
            showDatePickerDialog(btnDepartureCal.id)
        }
        btnArrivalCal.setOnClickListener {
            showDatePickerDialog(btnArrivalCal.id)
        }

        return view
    }

    fun showDatePickerDialog(viewid: Int) {
        // 클릭한 버튼이 무엇인지 dialogCalendar에 전달
        val dialogCalendar = DialogCalFragment()
        val bundle = Bundle()
        bundle.putInt("button", viewid)
        dialogCalendar.arguments = bundle
        dialogCalendar.show(parentFragmentManager, "datePicker")
    }

    fun setDate(viewid: Int, date: String) {
        // 클릭한 버튼에 날짜를 설정
        when (viewid) {
            R.id.btnDepartureCal -> {
                btnDepartureCal.text = date
            }
            R.id.btnArrivalCal -> {
                btnArrivalCal.text = date
            }
        }
    }
}