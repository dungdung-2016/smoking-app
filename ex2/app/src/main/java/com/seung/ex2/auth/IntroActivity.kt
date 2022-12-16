package com.seung.ex2.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seung.ex2.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    var backKeyPressedTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        button_login.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        button_signup.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
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