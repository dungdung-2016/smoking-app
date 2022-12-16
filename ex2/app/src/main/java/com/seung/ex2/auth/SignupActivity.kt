package com.seung.ex2.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.seung.ex2.InputDataActivity2
import com.seung.ex2.R
import com.seung.ex2.data.UserData
import com.seung.ex2.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth

        signup_okButton.setOnClickListener {
            createAccount(email.text.toString(),password.text.toString()
                ,userId.text.toString(),userName.text.toString())
        }
    }

    private fun createAccount(email: String, password: String, id: String, name: String) {

        if (email.isNotEmpty() && password.isNotEmpty() &&id.isNotEmpty() && name.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //유저 정보 가져오기
                        val uid= FirebaseUtils.getUid()
                        //데이터베이스 가져오기
                        val database= FirebaseDatabase.getInstance()
                        val myRef=database.getReference()
                        //유저 정보 저장
                        val dataInput= UserData(uid,email,id,name)
                        myRef.child("users").child(uid).child("userData").setValue(dataInput)

                        //메시지 출력
                        Toast.makeText(
                            this, "계정 생성 완료!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this, InputDataActivity2::class.java))
                        /*val intent = Intent(this, InputDataActivity2::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("password",password)
                        startActivity(intent)*/
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this,IntroActivity::class.java))
                    }
                }
        }
    }
}