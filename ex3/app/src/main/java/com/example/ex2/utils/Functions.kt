package com.example.ex2.utils

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ex2.data.SmokingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_input_data.*
import java.text.SimpleDateFormat
import java.util.*

class Functions {

    companion object {

        fun calculateDate(dateString:String?) : Int {
            val todaydate= Calendar.getInstance()
            var formatter = SimpleDateFormat("yyyy년 mm월 dd일")
            val date = formatter.parse(dateString)
            return (((todaydate.time.time - date.time)/ (60 * 60 * 24 * 1000))-272).toInt()
        }

        fun totalSmokingCount(startDate:String?,smokingCount:Int?): Int {
            val dayCount= calculateDate(startDate)
            return dayCount*smokingCount!!
        }

    }
}