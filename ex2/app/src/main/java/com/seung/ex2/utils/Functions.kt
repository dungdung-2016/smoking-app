package com.seung.ex2.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Functions {

    companion object {

        fun calculateDate(dateString:String?) : Int {
            val todaydate= Calendar.getInstance()
            var date = SimpleDateFormat("yyyy년 MM월 dd일").parse(dateString)
            //var date = formatter.parse(dateString)
            Log.d("todaydate: ",todaydate.toString())
            Log.d("todaydate: ",date.toString())
            Log.d("StartDate: ",dateString.toString())
            return (((todaydate.time.time - date.time)/ (60 * 60 * 24 * 1000))).toInt()
        }

        fun totalSmokingCount(startDate:String?,smokingCount:Int?): Int {
            val dayCount= calculateDate(startDate)
            return dayCount*smokingCount!!
        }

    }
}