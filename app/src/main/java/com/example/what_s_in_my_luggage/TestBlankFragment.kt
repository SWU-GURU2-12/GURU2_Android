package com.example.what_s_in_my_luggage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class TestBlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_test_blank, container, false)
        var textview = view?.findViewById<TextView>(R.id.textView)

        // bundle로 받은 데이터
        val userName = arguments?.getString("userName")
        val title = arguments?.getString("title")
        val destination = arguments?.getString("destination")
        val schedule = arguments?.getString("schedule")

        textview?.text = "userName: $userName,\n title: $title,\n destination: $destination,\n schedule: $schedule"

        return view
    }
}