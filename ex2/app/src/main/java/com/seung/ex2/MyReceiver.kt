package com.seung.ex2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seung.ex2.Constant.Companion.CHANNEL_ID
import com.seung.ex2.Constant.Companion.NOTIFICATION_ID
import com.seung.ex2.data.AccumulatedSmokingCount
import com.seung.ex2.data.UserData
import com.seung.ex2.utils.FirebaseUtils
import kotlinx.android.synthetic.main.fragment_home.*

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager
    var title: String=""
    var message: String=""

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)
    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "채널 이름입니다.",
                NotificationManager.IMPORTANCE_HIGH

            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "채널의 상세정보입니다."
            notificationManager.createNotificationChannel(
                notificationChannel)
        }
    }

    private fun deliverNotification(context: Context){

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.child("userData").getValue(UserData::class.java)
                    val ASC=snapshot.child("AccumulatedSmokingCount").getValue(AccumulatedSmokingCount::class.java)
                    if(UserDataFromFB?.user_id!=null&&ASC?.count!=null) {
                        title = UserDataFromFB?.user_id + "님은 현재 "+ASC.count.toString()+"개비 금연 하셨어요!"
                        message = "오늘 금연 일기를 작성하고 금연량을 늘려보세요!"


                        val contentIntent = Intent(context, NaviActivity::class.java)
                        val contentPendingIntent = PendingIntent.getActivity(
                            context,
                            NOTIFICATION_ID,
                            contentIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.nosmokingicon)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setContentIntent(contentPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)

                        notificationManager.notify(NOTIFICATION_ID, builder.build())
                    }
                }
            })
    }
}

class Constant {
    companion object {
        // 아이디 선언
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "notification_channel"

        // 알림 시간 설정
        const val ALARM_TIMER = 5
    }
}