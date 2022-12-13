package com.example.ex2.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.ex2.InputActivity2
import com.example.ex2.InputDataActivity
import com.example.ex2.R
import com.example.ex2.UserPageActivity
import com.example.ex2.adapter.RankingAdapter
import com.example.ex2.auth.IntroActivity
import com.example.ex2.data.AccumulatedSmokingCount
import com.example.ex2.data.RankingData
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.example.ex2.utils.Functions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_ranking.*

class RankingFragment : Fragment() {

    private var auth : FirebaseAuth? = null

    val list_ranking=ArrayList<RankingData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseDatabase.getInstance().getReference().child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        val rankingdata= RankingData()
                        rankingdata.uid=item.child("userData")?.getValue(UserData::class.java)?.uid.toString()
                        rankingdata.user_id=item.child("userData")?.getValue(UserData::class.java)?.user_id.toString()
                        if(item.child("smokingData")?.getValue(
                                SmokingData::class.java)?.start_date!=null&&item.child("smokingData")?.getValue(
                                SmokingData::class.java)!!.smoking_count!=null) {
                            var startDate = item.child("smokingData")?.getValue(
                                SmokingData::class.java
                            )?.start_date
                            var smokingCount = item.child("smokingData")?.getValue(
                                SmokingData::class.java
                            )!!.smoking_count
                            var dayCount = Functions.calculateDate(startDate)
                            rankingdata.period = dayCount
                            if(item.child("AccumulatedSmokingCount")?.getValue(AccumulatedSmokingCount::class.java)?.count==null){
                                rankingdata.total_smoking_count=0
                            }
                            else {
                                rankingdata.total_smoking_count =
                                    item.child("AccumulatedSmokingCount")
                                        ?.getValue(AccumulatedSmokingCount::class.java)!!.count
                            }
                            list_ranking.add(rankingdata)
                        }
                    }
                    list_ranking.sortBy { it.total_smoking_count }
                    list_ranking.reverse()
                    val list_adapter= RankingAdapter(activity!!,list_ranking)
                    listview_id.adapter=list_adapter
                }
            })

        listview_id.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val selected_uid=list_ranking[i].uid
                if(selected_uid!=null) {
                    val intent = Intent(activity!!, UserPageActivity::class.java)
                    intent.putExtra("selectedUid", selected_uid.toString())
                    startActivity(intent)
                }
            })

    }
}