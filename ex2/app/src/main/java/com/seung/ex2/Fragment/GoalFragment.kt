package com.seung.ex2.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seung.ex2.*
import com.seung.ex2.adapter.GoalAdapter
import com.seung.ex2.data.GoalData
import com.seung.ex2.data.SmokingData
import com.seung.ex2.utils.FirebaseUtils
import com.seung.ex2.utils.Functions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_goal.*

class GoalFragment : Fragment() {

    val list_goal = ArrayList<GoalData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseDatabase.getInstance().getReference().child("goals")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        val goaldata=GoalData()
                        goaldata.effect=item.getValue(GoalData::class.java)!!.effect
                        goaldata.days=item.getValue(GoalData::class.java)!!.days
                        list_goal.add(goaldata)
                    }
                }
            })

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("smokingData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val SmokingDataFromFB=snapshot.getValue(SmokingData::class.java)

                    if(SmokingDataFromFB?.start_date!=null) {
                        val day_count = Functions.calculateDate(SmokingDataFromFB!!.start_date)

                        for(item in list_goal){
                            if(day_count>=item.days){
                                item.successed=1
                            }
                        }
                    }

                    val goal_adapter= GoalAdapter(activity!!,list_goal)
                    listview_id.adapter=goal_adapter
                }
            })

    }
}