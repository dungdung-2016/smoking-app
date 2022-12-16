package com.seung.ex2

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.seung.ex2.Constant.Companion.ALARM_TIMER
import com.seung.ex2.Constant.Companion.NOTIFICATION_ID
import com.seung.ex2.Fragment.*
import com.seung.ex2.databinding.ActivityNaviBinding
import kotlinx.android.synthetic.main.activity_navi.*
import java.util.*


private const val TAG_GOAL = "goal_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_DIARY = "diary_fragment"
private const val TAG_SETTING = "setting_fragment"
private const val TAG_RANKING = "ranking_fragment"

internal var alarmManager: AlarmManager? = null

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    var backKeyPressedTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this,MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val repeatInterval : Long = INTERVAL_DAY
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY,9)
            set(Calendar.MINUTE,0)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            repeatInterval,
            pendingIntent)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_fragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.ranking_fragment -> setFragment(TAG_RANKING, RankingFragment())
                R.id.diary_fragment -> setFragment(TAG_DIARY, DiaryFragment())
                R.id.goal_fragment -> setFragment(TAG_GOAL, GoalFragment())
                R.id.setting_fragment-> setFragment(TAG_SETTING, SettingFragment())
            }
            true
        }

        swipeRefreshLayout.setOnRefreshListener{
            swipeRefreshLayout.isRefreshing=false
            finish()
            startActivity(Intent(this, NaviActivity::class.java))
        }

    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val goal = manager.findFragmentByTag(TAG_GOAL)
        val ranking = manager.findFragmentByTag(TAG_RANKING)
        val diary = manager.findFragmentByTag(TAG_DIARY)
        val home = manager.findFragmentByTag(TAG_HOME)
        val setting = manager.findFragmentByTag(TAG_SETTING)


        if (goal != null){
            fragTransaction.hide(goal)
        }

        if (home != null){
            fragTransaction.hide(home)
        }

        if (diary != null){
            fragTransaction.hide(diary)
        }

        if (setting != null) {
            fragTransaction.hide(setting)
        }

        if (ranking != null){
            fragTransaction.hide(ranking)
        }

        if (tag == TAG_GOAL) {
            if (goal!=null){
                fragTransaction.show(goal)
            }
        }
        else if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_DIARY) {
            if (diary != null) {
                fragTransaction.show(diary)
            }
        }

        else if (tag == TAG_RANKING){
            if (ranking != null){
                fragTransaction.show(ranking)
            }
        }

        else if (tag == TAG_SETTING){
            if(setting!=null){
                fragTransaction.show(setting)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }

    override fun onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime +2500){
            binding.navigationView.selectedItemId=R.id.home_fragment
            backKeyPressedTime = System.currentTimeMillis()
            return;
        }
        if (System.currentTimeMillis() <=backKeyPressedTime+2500){
            finishAffinity()
        }
    }
}