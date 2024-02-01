package com.example.what_s_in_my_luggage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class BottomNavigationFragment : Fragment() {

    companion object {
        fun newInstance(selectedButtonId: Int): BottomNavigationFragment {
            val fragment = BottomNavigationFragment()
            val args = Bundle()
            args.putInt("SELECTED_BUTTON_ID", selectedButtonId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var btnNaviMain: ImageButton
    private lateinit var btnNaviTemplate: ImageButton
    private lateinit var btnNaviStored: ImageButton
    private lateinit var btnNaviMyroom: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false)

        btnNaviMain = view.findViewById(R.id.btnNaviMain)
        btnNaviTemplate = view.findViewById(R.id.btnNaviTemplete)
        btnNaviStored = view.findViewById(R.id.btnNaviStored)
        btnNaviMyroom = view.findViewById(R.id.btnNaviMyroom)

        setButtonListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreSelectedButtonState()
    }

    private fun restoreSelectedButtonState() {
        val sharedPrefs = requireActivity().getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        val selectedButtonId = sharedPrefs.getInt("SELECTED_BUTTON_ID", R.id.btnNaviMain)
        view?.findViewById<ImageButton>(selectedButtonId)?.isSelected = true
    }

    private fun setButtonListeners() {
        val buttons = listOf(btnNaviMain, btnNaviTemplate, btnNaviStored, btnNaviMyroom)

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { it.isSelected = false }
                button.isSelected = true

                when (button.id) {
                    R.id.btnNaviMain -> navigateToActivity(MainActivity::class.java)
                    R.id.btnNaviTemplete -> navigateToActivity(CommunityActivity::class.java)
                    R.id.btnNaviStored -> navigateToActivity(StoredActivity::class.java)
                    R.id.btnNaviMyroom -> navigateToActivity(MyRoomActivity::class.java)
                }
                saveSelectedButtonId(button.id)
            }
        }
    }

    private fun <T> navigateToActivity(activityClass: Class<T>) {
        val intent = Intent(context, activityClass)
        startActivity(intent)
    }

    private fun saveSelectedButtonId(buttonId: Int) {
        val sharedPrefs = requireActivity().getSharedPreferences("BottomNavPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putInt("SELECTED_BUTTON_ID", buttonId).apply()
    }
}
