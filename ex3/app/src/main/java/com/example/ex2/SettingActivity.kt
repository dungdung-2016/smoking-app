package com.example.ex2

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ex2.adapter.SettingAdapter
import com.example.ex2.auth.IntroActivity
import com.example.ex2.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_ranking.navigationView
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {

    val list_setting=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        list_setting.add("금연 정보 입력하기")
        list_setting.add("사용자 정보 변경하기")
        list_setting.add("로그아웃")
        list_setting.add("회원탈퇴")

        val setting_adapter= SettingAdapter(this@SettingActivity,list_setting)
        listview_setting.adapter=setting_adapter

        listview_setting.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            if(i==0){startActivity(Intent(this@SettingActivity,InputDataActivity::class.java))}
            else if(i==1){startActivity(Intent(this@SettingActivity,InputActivity2::class.java))}
            else if(i==2){
                FirebaseAuth.getInstance()?.signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            else if(i==3){
                val uid= FirebaseUtils.getUid()
                FirebaseDatabase.getInstance().getReference(uid).removeValue()
                FirebaseAuth.getInstance()?.currentUser!!.delete()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
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