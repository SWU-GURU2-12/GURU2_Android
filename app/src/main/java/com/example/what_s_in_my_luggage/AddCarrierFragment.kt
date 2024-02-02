package com.example.what_s_in_my_luggage

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.google.firebase.Firebase
import com.google.firebase.database.database

class AddCarrierFragment : Fragment() {
    private val databaseReference = Firebase.database.getReference("Luggage")

    private lateinit var btnDepartureCal: Button
    private lateinit var btnArrivalCal: Button
    private lateinit var travelPlace: CardView
    private lateinit var template: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fragment Result API
        setFragmentResultListener("selectDate") { key, bundle ->
            val date = bundle.getString("date")
            val button = bundle.getInt("button")
            setDate(button, date!!)
        }
        setFragmentResultListener("selectPlace") { key, bundle ->
            val place = bundle.getString("place")
            setTravelPlace(place!!)
        }
    }

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
        travelPlace = view.findViewById<CardView>(R.id.travelPlace)
        template = view.findViewById<CardView>(R.id.template)

        // 가는 날, 오는 날 -> 달력 다이얼로그 띄우기
        btnDepartureCal.setOnClickListener {
            showDatePickerDialog(btnDepartureCal.id)
        }
        btnArrivalCal.setOnClickListener {
            showDatePickerDialog(btnArrivalCal.id)
        }

        // 여행지
        travelPlace.setOnClickListener {
            val dialogTravelPlace = DialogListViewFragment()
            dialogTravelPlace.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            dialogTravelPlace.show(parentFragmentManager, "travelPlace")
        }

        // 템플릿


        // TODO: next button => 가는 날, 오는 날 계산 (가는 날 < 오는 날)

        return view
    }

    fun showDatePickerDialog(viewid: Int) {
        // 클릭한 버튼이 무엇인지 dialogCalendar에 전달
        val dialogCalendar = DialogCalFragment()
        val bundle = Bundle()
        bundle.putInt("button", viewid)
        dialogCalendar.arguments = bundle
        dialogCalendar.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        dialogCalendar.show(parentFragmentManager, "datePicker")
    }

    fun setDate(viewid: Int, date: String) {
        // 클릭한 버튼에 날짜를 설정
        when (viewid) {
            R.id.btnDepartureCal -> {
                btnDepartureCal.text = date
                btnDepartureCal.setTextColor(Color.BLACK)
            }
            R.id.btnArrivalCal -> {
                btnArrivalCal.text = date
                btnArrivalCal.setTextColor(Color.BLACK)
            }
        }
    }

    fun setTravelPlace(place: String) {
        val txtTravelPlace = travelPlace.findViewById<TextView>(R.id.txtTravelPlace)
        txtTravelPlace.text = place
        txtTravelPlace.setTextColor(Color.BLACK)
    }
}