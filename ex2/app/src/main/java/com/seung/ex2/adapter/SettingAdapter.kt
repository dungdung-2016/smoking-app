package com.seung.ex2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.seung.ex2.R
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
                /*val view: View = LayoutInflater.from(context).inflate(R.layout.setting_item,null)

                view.settingType.setText(list_setting.get(p0))

                return view*/
                val view : View
                val holder : ViewHolder

                if (p1 == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.setting_item, null)
                        holder = ViewHolder()
                        holder.settingType = view.findViewById(R.id.settingType)

                        view.tag = holder
                        /* convertView가 null, 즉 최초로 화면을 실행할 때에
                        ViewHolder에 각각의 TextView와 ImageView를 findVidwById로 설정.
                        마지막에 태그를 holder로 설정한다. */

                } else {
                        holder = p1.tag as ViewHolder
                        view = p1
                        /* 이미 만들어진 View가 있으므로, tag를 통해 불러와서 대체한다. */
                }

                val setting = list_setting.get(p0)

                /*val resourceId = context.resources.getIdentifier(dog.photo, "drawable", context.packageName)
                holder.dogPhoto?.setImageResource(resourceId)
                holder.dogBreed?.text = dog.breed
                holder.dogAge?.text = dog.dogAge
                holder.dogGender?.text = dog.gender
                holder와 실제 데이터를 연결한다. null일 수 있으므로 변수에 '?'을 붙여 safe call 한다. */
                holder.settingType?.text = setting

                return view

        }

        private class ViewHolder {
                var settingType : TextView? = null
        }
}