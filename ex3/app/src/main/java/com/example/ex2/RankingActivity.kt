package com.example.ex2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import com.example.ex2.adapter.RankingAdapter
import com.example.ex2.data.RankingData
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.Functions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_ranking.navigationView

class RankingActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    val list_ranking=ArrayList<RankingData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        FirebaseDatabase.getInstance().getReference().child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        val rankingdata= RankingData()
                        rankingdata.uid=item.child("userData")?.getValue(UserData::class.java)?.uid.toString()
                        rankingdata.user_id=item.child("userData")?.getValue(UserData::class.java)?.user_id.toString()
                        var startDate=item.child("smokingData")?.getValue(
                            SmokingData::class.java)?.start_date
                        var smokingCount=item.child("smokingData")?.getValue(
                            SmokingData::class.java)!!.smoking_count
                        var dayCount=Functions.calculateDate(startDate)

                        rankingdata.total_smoking_count=smokingCount*dayCount
                        list_ranking.add(rankingdata)
                    }
                    list_ranking.sortBy { it.total_smoking_count }
                    list_ranking.reverse()
                    val list_adapter= RankingAdapter(this@RankingActivity,list_ranking)
                    listview_id.adapter=list_adapter
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