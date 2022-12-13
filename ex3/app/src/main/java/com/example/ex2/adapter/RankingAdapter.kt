package com.example.ex2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.ex2.R
import com.example.ex2.data.RankingData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.ranking_item.view.*
import androidx.appcompat.app.AppCompatActivity

class RankingAdapter (val context: Context, val list_ranking: ArrayList<RankingData>): BaseAdapter(){
    override fun getCount(): Int {
        return list_ranking.size
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        //p0는 인덱스
        val view: View = LayoutInflater.from(context).inflate(R.layout.ranking_item,null)

        view.ranking.setText("${p0+1}등")
        view.userdata.setText("id: ${list_ranking.get(p0).user_id}   금연한 담배 수: ${list_ranking.get(p0).total_smoking_count}")

        val myImage = view.findViewById<ImageView>(R.id.userprofile)

        val storageRef = Firebase.storage.reference.child(list_ranking.get(p0).uid + ".png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if(task.isSuccessful) {
                Glide.with(view)
                    .load(task.result)
                    .into(myImage)

            }

        })
        return view
    }

}