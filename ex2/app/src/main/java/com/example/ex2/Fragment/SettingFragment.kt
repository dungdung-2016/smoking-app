package com.example.ex2.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.ex2.InputActivity2
import com.example.ex2.InputDataActivity
import com.example.ex2.NaviActivity
import com.example.ex2.R
import com.example.ex2.adapter.SettingAdapter
import com.example.ex2.adapter.SettingRecyclerViewAdapter
import com.example.ex2.auth.IntroActivity
import com.example.ex2.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment() {

    val list_setting=ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_setting.add("금연 정보 입력하기")
        list_setting.add("사용자 정보 변경하기")
        list_setting.add("로그아웃")
        list_setting.add("회원탈퇴")

        //val setting_adapter= SettingAdapter(activity!!,list_setting)
        val setting_adapter=SettingAdapter(activity!!,list_setting)
        listview_setting.adapter=setting_adapter

        listview_setting.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if (i == 0) {
                startActivity(Intent(activity!!, InputDataActivity::class.java))
            } else if (i == 1) {
                startActivity(Intent(activity!!, InputActivity2::class.java))
            } else if (i == 2) {
                FirebaseAuth.getInstance()?.signOut()
                val intent = Intent(activity!!, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else if (i == 3) {
                val uid = FirebaseUtils.getUid()
                FirebaseDatabase.getInstance().getReference(uid).removeValue()
                FirebaseAuth.getInstance()?.currentUser!!.delete()
                val intent = Intent(activity!!, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        })
    }
}