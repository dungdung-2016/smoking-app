package com.example.ex2

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ex2.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_setting.*


class Fragment1 : Fragment() {

    lateinit var naviActivity: NaviActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        naviActivity = context as NaviActivity
    }

    val list_setting=ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        list_setting.add("금연 정보 입력하기")
        list_setting.add("사용자 정보 변경하기")
        list_setting.add("로그인")
        list_setting.add("로그아웃")
        val listView=listview_setting

        val setting_adapter= SettingAdapter(naviActivity,list_setting)
        listView.adapter=setting_adapter

        return inflater.inflate(com.example.ex2.R.layout.fragment_1, container, false)
    }
}