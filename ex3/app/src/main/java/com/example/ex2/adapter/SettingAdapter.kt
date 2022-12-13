package com.example.ex2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.ex2.InputDataActivity
import com.example.ex2.R
import kotlinx.android.synthetic.main.setting_item.view.*

class SettingAdapter (val context: Context, val list_setting: ArrayList<String>): BaseAdapter(){
        override fun getCount(): Int {
                return list_setting.size
        }

        override fun getItem(p0: Int): Any {
                return 0
        }

        override fun getItemId(p0: Int): Long {
                return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
                //p0는 인덱스
                val view: View = LayoutInflater.from(context).inflate(R.layout.setting_item,null)

                view.settingType.setText(list_setting.get(p0))

                return view
        }

}