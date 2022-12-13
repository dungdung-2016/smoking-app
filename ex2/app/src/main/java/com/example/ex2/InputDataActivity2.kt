package com.example.ex2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ex2.auth.IntroActivity
import com.example.ex2.data.SmokingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_input_data.*
import java.util.*

class InputDataActivity2 : AppCompatActivity() {
    private var uid : String=""
    private var auth : FirebaseAuth? = null

    var dateString:String=""
    var calculateDate:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        auth = FirebaseAuth.getInstance()

        uid=auth?.currentUser?.uid.toString()

        button_input.setOnClickListener{
            val database= FirebaseDatabase.getInstance()
            val myRef=database.getReference()
            //smoking data 저장
            val start_date=dateString
            //val day_count=calculateDate
            var smoking_count=ciga_num.text.toString().toInt()
            //var total_smoking_count=smoking_count*day_count
            //var money_count=ciga_price.text.toString().toInt()
            //var money_saved=money_count*total_smoking_count
            //var life_saved=day_count*smoking_count*12

            if(ciga_num!=null&&start_date!=null) {
                val dataInput = SmokingData(start_date, smoking_count)

                myRef.child("users").child(uid).child("smokingData").setValue(dataInput)

                val intent = Intent(this, NaviActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"금연정보를 제대로 입력해주세요!",Toast.LENGTH_LONG)
            }
        }

        button_setdate.setOnClickListener{
            //오늘날짜
            val todaydate = Calendar.getInstance()
            //시작날짜
            val selectdate = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                result.setText(dateString)
            }

            var dpd= DatePickerDialog(this, selectdate, todaydate.get(Calendar.YEAR),todaydate.get(
                Calendar.MONTH),todaydate.get(Calendar.DAY_OF_MONTH))
            dpd.datePicker.maxDate = System.currentTimeMillis() - 1000;
            dpd.show()
        }
    }
}