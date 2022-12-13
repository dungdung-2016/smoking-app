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
import kotlinx.android.synthetic.main.fragment_home.*

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
        view.userdata.setText("id: ${list_ranking.get(p0).user_id}   금연량: ${list_ranking.get(p0).total_smoking_count}")

        val myImage = view.findViewById<ImageView>(R.id.userprofile)

        if(list_ranking.get(p0).period<3){
            view.rank.setImageResource(R.drawable.iron)
        }
        else if(list_ranking.get(p0).period<10){
            view.rank.setImageResource(R.drawable.bronze)
        }
        else if(list_ranking.get(p0).period<30){
            view.rank.setImageResource(R.drawable.silver)
        }
        else if(list_ranking.get(p0).period<100){
            view.rank.setImageResource(R.drawable.gold)
        }
        else if(list_ranking.get(p0).period<365){
            view.rank.setImageResource(R.drawable.platinum)
        }
        else if(list_ranking.get(p0).period<1825){
            view.rank.setImageResource(R.drawable.diamond)
        }
        else if(list_ranking.get(p0).period>=1825){
            view.rank.setImageResource(R.drawable.immortal)
        }

        val storageRef = Firebase.storage.reference.child(list_ranking.get(p0).uid + ".png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if(task.isSuccessful) {
                Glide.with(view)
                    .load(task.result)
                    .into(myImage)

            }
            else{
                view.userprofile.setImageResource((R.drawable.profile_image))
            }

        })
        return view
    }

}