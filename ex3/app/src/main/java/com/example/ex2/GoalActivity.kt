package com.example.ex2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ex2.adapter.GoalAdapter
import com.example.ex2.adapter.RankingAdapter
import com.example.ex2.data.GoalData
import com.example.ex2.data.RankingData
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.example.ex2.utils.Functions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_ranking.navigationView

class GoalActivity : AppCompatActivity() {

    val list_goal = ArrayList<GoalData>()

    //val cur_uid = intent.getStringExtra("key").toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        FirebaseDatabase.getInstance().getReference().child("goals")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        val goaldata=GoalData()
                        goaldata.effect=item.getValue(GoalData::class.java)!!.effect
                        goaldata.days=item.getValue(GoalData::class.java)!!.days
                        list_goal.add(goaldata)
                    }
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
                        val day_count = Functions.calculateDate(SmokingDataFromFB!!.start_date)

                        for(item in list_goal){
                            if(day_count>=item.days){
                                item.successed=1
                            }
                        }
                    }

                    val goal_adapter= GoalAdapter(this@GoalActivity,list_goal)
                    listview_id.adapter=goal_adapter
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
        startActivity(Intent(this,MainActivity::class.java))
    }
}