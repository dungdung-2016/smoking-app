package com.example.ex2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.ex2.R
import com.example.ex2.data.GoalData
import com.example.ex2.data.RankingData
import kotlinx.android.synthetic.main.goal_item.view.*

class GoalAdapter (val context: Context, val list_goal: ArrayList<GoalData>): BaseAdapter() {
    override fun getCount(): Int {
        return list_goal.size
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //p0는 인덱스
        val view: View = LayoutInflater.from(context).inflate(R.layout.goal_item, null)

        view.days.setText("${list_goal.get(p0)!!.days}일")
        view.effect.setText(list_goal.get(p0)!!.effect)
        if(list_goal.get(p0)!!.successed == 1) {
            view.done.setImageResource(R.drawable.ic_baseline_check_24)
        }
        else {view.done.setImageResource(R.drawable.ic_baseline_clear_24) }
        return view
    }
}
