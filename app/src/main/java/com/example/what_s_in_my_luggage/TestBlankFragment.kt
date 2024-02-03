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

        // test 데이터
        var dataManager = UserDataManager.getInstance(requireContext())
        var temp = dataManager.getTempLuggage()

        textview?.text = "test: ${temp?.luggageID}, \n${temp?.userName}"


        return view
    }
}