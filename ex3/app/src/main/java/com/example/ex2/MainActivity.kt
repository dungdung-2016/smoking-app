package com.example.ex2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ex2.auth.IntroActivity
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.example.ex2.utils.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    var backKeyPressedTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        val myImage = findViewById<ImageView>(R.id.myImage)

        val storageRef = Firebase.storage.reference.child(FirebaseUtils.getUid() + ".png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if(task.isSuccessful) {
                Glide.with(baseContext)
                    .load(task.result)
                    .into(myImage)

            }

        })


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.getValue(UserData::class.java)

                    userdata_value.setText("이메일 : "+UserDataFromFB?.user_email
                            +"\n아이디 : "+UserDataFromFB?.user_id
                            +"\n이름 : "+UserDataFromFB?.user_name)
                }
            })

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("smokingData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val SmokingDataFromFB=snapshot.getValue(SmokingData::class.java)
                    if(SmokingDataFromFB?.start_date!=null) {
                        val day_count = Functions.calculateDate(SmokingDataFromFB?.start_date)
                        val total_smoking_count = Functions.totalSmokingCount(
                            SmokingDataFromFB?.start_date,
                            SmokingDataFromFB?.smoking_count
                        )

                        smokingdata_value.setText("금연일 : "+SmokingDataFromFB?.start_date
                            +"\n금연한 기간 : "+day_count+"일"
                            +"\n금연한 담배 갯수 : "+total_smoking_count+"개비"
                            +"\n절약한 금액 : "+total_smoking_count*4500+"원"
                            +"\n연장된 수명 : "+total_smoking_count*12+"분")
                    }
                }
            })

        navigationView.setOnItemSelectedListener{item->
            when(item.itemId) {
                R.id.home_fragment -> {startActivity(Intent(this,MainActivity::class.java))}
                R.id.ranking_fragment -> {startActivity(Intent(this,RankingActivity::class.java))}
                R.id.goal_fragment -> {startActivity(Intent(this,GoalActivity::class.java))}
                R.id.setting_fragment-> {startActivity(Intent(this,SettingActivity::class.java))}
            }
            true
        }

    }

    override fun onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime +2500){
            backKeyPressedTime=System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() <=backKeyPressedTime+2500){
            finishAffinity()
        }
    }
}