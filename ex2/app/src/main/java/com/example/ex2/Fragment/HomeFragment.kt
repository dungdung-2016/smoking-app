package com.example.ex2.Fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.ex2.R
import com.example.ex2.data.AccumulatedSmokingCount
import com.example.ex2.data.MemoData
import com.example.ex2.data.SmokingData
import com.example.ex2.data.UserData
import com.example.ex2.utils.FirebaseUtils
import com.example.ex2.utils.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream

class HomeFragment : Fragment() {

    private var auth : FirebaseAuth? = null

    var progress:Int=0

    val accumulatedSmokingCount= AccumulatedSmokingCount()
    var avgSmokingCount:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth=FirebaseAuth.getInstance()

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                myImageHome.setImageURI(uri)
            }
        )

        val uid=FirebaseUtils.getUid()

        myImageHome.setOnClickListener {
            getAction.launch("image/*")
            uploadImage(uid)
        }

        val storageRef = Firebase.storage.reference.child(FirebaseUtils.getUid() + ".png")
        storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if(task.isSuccessful) {
                Glide.with(activity!!)
                    .load(task.result)
                    .into(myImageHome)

            }
            else{
                myImageHome.setImageResource(R.drawable.profile_image)
            }

        })

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("userData")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.getValue(UserData::class.java)
                    if(UserDataFromFB?.user_id!=null) {
                        userdata_value_id.setText(UserDataFromFB?.user_id + " 님 ")
                    }
                }
            })

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val SmokingDataFromFB=snapshot.child("smokingData").getValue(SmokingData::class.java)
                    if(SmokingDataFromFB?.start_date!=null) {
                        val day_count = Functions.calculateDate(SmokingDataFromFB?.start_date)
                        Log.d("daycount: ",day_count.toString())
                        var total_smoking_count=0
                        if(snapshot.child("AccumulatedSmokingCount").getValue(AccumulatedSmokingCount::class.java)?.count==null){
                            total_smoking_count=0
                        }
                        else{
                            total_smoking_count=snapshot.child("AccumulatedSmokingCount").getValue(AccumulatedSmokingCount::class.java)!!.count
                        }
                        if(day_count<3){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.iron))
                            progress=3
                        }
                        else if(day_count<10){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.bronze))
                            progress=10
                        }
                        else if(day_count<30){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.silver))
                            progress=30
                        }
                        else if(day_count<100){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.gold))
                            progress=100
                        }
                        else if(day_count<365){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.platinum))
                            progress=365
                        }
                        else if(day_count<1825){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.diamond))
                            progress=1825
                        }
                        else if(day_count>=1825){
                            rank.setImageDrawable(getResources().getDrawable(R.drawable.immortal))
                            progress=day_count
                        }

                        progressbar_period.progress=((day_count*100)/progress)

                        smokingdata_value_period.setText("금연 시작일\n"+SmokingDataFromFB?.start_date
                                +"\n금연 기간\n"+day_count+"일")
                        progressbar_total_smoking_count.progress=((day_count*100)/progress)
                        smokingdata_total_smoking_count.setText(
                                "금연한 담배\n"+total_smoking_count+"개비")
                        progressbar_saved_money.progress=((day_count*100)/progress)
                        smokingdata_money_saved.setText(
                                "절약한 금액\n"+total_smoking_count*225+"원")
                        progressbar_saved_life.progress=((day_count*100)/progress)
                        smokingdata_life_saved.setText(
                                "연장된 수명\n"+total_smoking_count*12+"분")
                    }
                }
            })
/*
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid()).child("diary")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //성공했을때
                    for (item in snapshot.getChildren()) {
                        Log.d("count: ",item.getValue(MemoData::class.java)?.smokingCount.toString())

                        accumulatedSmokingCount.count += (avgSmokingCount-item.getValue(MemoData::class.java)?.smokingCount.toString()
                            .toInt())
                        Log.d("totalcount: ",accumulatedSmokingCount.count.toString())
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
                            .child("AccumulatedSmokingCount").setValue(accumulatedSmokingCount)
                    }
                }
            })
*/
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //실패했을때
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    avgSmokingCount=snapshot.child("smokingData").getValue(SmokingData::class.java)!!.smoking_count
                    Log.d("avgSC: ",avgSmokingCount.toString())
                    //성공했을때
                    for (item in snapshot.child("diary").getChildren()) {
                        Log.d("count: ",item.getValue(MemoData::class.java)?.smokingCount.toString())

                        accumulatedSmokingCount.count += (avgSmokingCount-item.getValue(MemoData::class.java)?.smokingCount.toString()
                            .toInt())
                        Log.d("totalcount: ",accumulatedSmokingCount.count.toString())
                        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
                            .child("AccumulatedSmokingCount").setValue(accumulatedSmokingCount)
                    }
                }
            })

    }
    private fun uploadImage(uid : String){

        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")


        // Get the data from an ImageView as bytes
        myImageHome.isDrawingCacheEnabled = true
        myImageHome.buildDrawingCache()
        val bitmap = (myImageHome.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("d", "failed")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.d("d", "successed")
        }


    }
}