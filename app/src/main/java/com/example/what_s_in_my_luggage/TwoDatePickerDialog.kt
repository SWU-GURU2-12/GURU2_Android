package com.example.what_s_in_my_luggage

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.what_s_in_my_luggage.R
import java.text.SimpleDateFormat
import java.util.Calendar

class TwoDatePickerDialog : AppCompatActivity(), View.OnClickListener {
    private lateinit var mStartTime: EditText
    private lateinit var mEndTime: EditText
    private lateinit var mDatePickerDialogFragment: DatePickerDialogFragment

    val FLAG_START_DATE = 0
    val FLAG_END_DATE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_date_picker_dialog)

        mStartTime = findViewById(R.id.start_date)
        mEndTime = findViewById(R.id.end_date)
        mDatePickerDialogFragment = DatePickerDialogFragment()

        mStartTime.setOnClickListener(this)
        mEndTime.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.start_date) {
            mDatePickerDialogFragment.setFlag(FLAG_START_DATE)
            mDatePickerDialogFragment.show(supportFragmentManager, "datePicker")
        } else if (id == R.id.end_date) {
            mDatePickerDialogFragment.setFlag(FLAG_END_DATE)
            mDatePickerDialogFragment.show(supportFragmentManager, "datePicker")
        }
    }

    inner class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
        private var flag = 0

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireActivity(), this, year, month, day)
        }

        fun setFlag(i: Int) {
            flag = i
        }

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val format = SimpleDateFormat("yyyy-MM-dd")
            if (flag == FLAG_START_DATE) {
                mStartTime.setText(format.format(calendar.time))
            } else if (flag == FLAG_END_DATE) {
                mEndTime.setText(format.format(calendar.time))
            }
        }
    }
}