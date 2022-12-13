package com.example.ex2

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ex2.data.AccumulatedSmokingCount
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.example.ex2.utils.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_user_page.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.progressbar_period
import kotlinx.android.synthetic.main.fragment_home.progressbar_saved_life
import kotlinx.android.synthetic.main.fragment_home.progressbar_saved_money
import kotlinx.android.synthetic.main.fragment_home.progressbar_total_smoking_count
import kotlinx.android.synthetic.main.fragment_home.rank
import kotlinx.android.synthetic.main.fragment_home.smokingdata_life_saved
import kotlinx.android.synthetic.main.fragment_home.smokingdata_money_saved
import kotlinx.android.synthetic.main.fragment_home.smokingdata_total_smoking_count
import kotlinx.android.synthetic.main.fragment_home.smokingdata_value_period
import kotlinx.android.synthetic.main.fragment_home.userdata_value_id


class UserPageActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    var backKeyPressedTime:Long=0

    var selected_uid:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        auth= FirebaseAuth.getInstance()
        if(intent.getStringExtra("selectedUid")==null)
            selected_uid = FirebaseUtils.getUid()
        else
            selected_uid = intent.getStringExtra("selectedUid").toString()

        val storageRef = Firebase.storage.reference.child(selected_uid + ".png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if(task.isSuccessful) {
                Glide.with(baseContext)
                    .load(task.result)
                    .into(userImage)

            }
            else{
                userImage.setImageResource(R.drawable.profile_image)
            }

        })


        FirebaseDatabase.getInstance().getReference().child("users").child(selected_uid!!)
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.getValue(UserData::class.java)

                    if(UserDataFromFB?.user_id!=null) {
                        userdata_value_id.setText(UserDataFromFB?.user_id + " 님 ")
                    }
                }
            })

        FirebaseDatabase.getInstance().getReference().child("users").child(selected_uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val SmokingDataFromFB=snapshot.child("smokingData").getValue(SmokingData::class.java)
                    if(SmokingDataFromFB?.start_date!=null) {
                        val day_count = Functions.calculateDate(SmokingDataFromFB?.start_date)
                        var total_smoking_count=0
                        if(snapshot.child("AccumulatedSmokingCount").getValue(AccumulatedSmokingCount::class.java)?.count==null){
                            total_smoking_count=0
                        }
                        else{
                            total_smoking_count=snapshot.child("AccumulatedSmokingCount").getValue(AccumulatedSmokingCount::class.java)!!.count
                        }
                        var progress=0
                        if(day_count<3){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.iron))
                            progress=3
                        }
                        else if(day_count<10){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.bronze))
                            progress=10
                        }
                        else if(day_count<30){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.silver))
                            progress=30
                        }
                        else if(day_count<100){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.gold))
                            progress=100
                        }
                        else if(day_count<365){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.platinum))
                            progress=365
                        }
                        else if(day_count<1825){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.diamond))
                            progress=1825
                        }
                        else if(day_count>=1825){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.immortal))
                            progress=day_count
                        }

                        progressbar_period.progress=((day_count*100)/progress)

                        smokingdata_value_period.setText("금연 시작일\n"+SmokingDataFromFB?.start_date
                                +"\n금연 기간\n"+day_count+"일")
                        progressbar_total_smoking_count.progress=((day_count*100)/progress)
                        smokingdata_total_smoking_count.setText(
                            "금연한 담배\n"+total_smoking_count+"개비")
                        progressbar_saved_money.progress=((day_count*100)/progress)
                        smokingdata_money_saved.setText(
                            "절약한 금액\n"+total_smoking_count*4500+"원")
                        progressbar_saved_life.progress=((day_count*100)/progress)
                        smokingdata_life_saved.setText(
                            "연장된 수명\n"+total_smoking_count*12+"분")
                    }
                }
            })
    }
}