package com.seung.ex2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seung.ex2.R
import kotlinx.android.synthetic.main.goal_item.view.*
import kotlinx.android.synthetic.main.setting_item.view.*

class SettingRecyclerViewAdapter(private val items:ArrayList<String>):
    RecyclerView.Adapter<SettingRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder:SettingRecyclerViewAdapter.ViewHolder,position:Int){
        val item=items[position]
        val listener=View.OnClickListener { it->

        }
        holder.apply{
            bind(listener,item)
            itemView.tag=item
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup,viewTpe:Int):
            SettingRecyclerViewAdapter.ViewHolder{
        val inflatedView=LayoutInflater.from(parent.context).inflate(R.layout.setting_item,parent,false)
        return SettingRecyclerViewAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        private var view:View=v
        fun bind(listener:View.OnClickListener,Item:String){
            view.settingType.setText(Item)
            view.setOnClickListener(listener)
        }
    }

}