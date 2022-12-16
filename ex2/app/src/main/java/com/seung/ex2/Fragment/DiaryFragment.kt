package com.seung.ex2.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seung.ex2.R
import com.seung.ex2.data.MemoData
import com.seung.ex2.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_diary.*

class DiaryFragment : Fragment() {

    lateinit var str: String
    lateinit var num: String
    val Memo= MemoData()
    var selectedDay: String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView.maxDate = System.currentTimeMillis() - 1000;
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            smokingCountText.visibility = View.VISIBLE
            smokingCountInput.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            smokingText.visibility = View.INVISIBLE
            smokingCount.visibility = View.INVISIBLE
            diaryContent.visibility = View.INVISIBLE
            diaryContent.text=null
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            selectedDay=String.format("%d%d%d", year, month + 1, dayOfMonth)
            contextEditText.setText("")
            smokingCountInput.setText("")
            lookup()
        }

        saveBtn.setOnClickListener {
            saveDiary()
            contextEditText.visibility = View.INVISIBLE
            smokingCountText.visibility = View.INVISIBLE
            smokingCountInput.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString()
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
            smokingText.visibility = View.VISIBLE
            num = smokingCount.text.toString()
            smokingCount.text = num
            smokingCount.visibility = View.VISIBLE
        }
    }

    // 달력 내용 조회, 수정
    fun lookup() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("diary").child(selectedDay)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val UserDataFromFB=snapshot.getValue(MemoData::class.java)
                    if(UserDataFromFB?.text!=null) {
                        smokingCount.text = UserDataFromFB.smokingCount.toString()
                        diaryContent.text = UserDataFromFB.text
                        Log.d("text: ",UserDataFromFB.text)
                    }
                    else{
                        diaryContent.visibility = View.INVISIBLE
                        smokingText.visibility = View.INVISIBLE
                        smokingCount.visibility = View.INVISIBLE
                        updateBtn.visibility = View.INVISIBLE
                        deleteBtn.visibility = View.INVISIBLE
                        diaryTextView.visibility = View.VISIBLE
                        saveBtn.visibility = View.VISIBLE
                        contextEditText.visibility = View.VISIBLE
                        smokingCountText.visibility = View.VISIBLE
                        smokingCountInput.visibility = View.VISIBLE
                    }
                }
            })
            if(diaryContent.text!=null) {
                contextEditText.visibility = View.INVISIBLE
                smokingCountText.visibility = View.INVISIBLE
                smokingCountInput.visibility = View.INVISIBLE
                diaryContent.visibility = View.VISIBLE
                smokingText.visibility = View.VISIBLE
                smokingCount.visibility = View.VISIBLE
                saveBtn.visibility = View.INVISIBLE
                updateBtn.visibility = View.VISIBLE
                deleteBtn.visibility = View.VISIBLE
                updateBtn.setOnClickListener {
                    contextEditText.visibility = View.VISIBLE
                    smokingCountText.visibility = View.VISIBLE
                    smokingCountInput.visibility = View.VISIBLE
                    diaryContent.visibility = View.INVISIBLE
                    smokingText.visibility = View.INVISIBLE
                    smokingCount.visibility = View.INVISIBLE
                    contextEditText.setText(diaryContent.text)
                    saveBtn.visibility = View.VISIBLE
                    updateBtn.visibility = View.INVISIBLE
                    deleteBtn.visibility = View.INVISIBLE
                    diaryContent.text = contextEditText.text
                }
                deleteBtn.setOnClickListener {
                    diaryContent.visibility = View.INVISIBLE
                    smokingText.visibility = View.INVISIBLE
                    smokingCount.visibility = View.INVISIBLE
                    updateBtn.visibility = View.INVISIBLE
                    deleteBtn.visibility = View.INVISIBLE
                    contextEditText.setText("")
                    contextEditText.visibility = View.VISIBLE
                    smokingCountText.visibility = View.VISIBLE
                    smokingCountInput.visibility = View.VISIBLE
                    saveBtn.visibility = View.VISIBLE
                    removeDiary()
                }
            }
            else if (diaryContent.text == null) {
                diaryContent.visibility = View.INVISIBLE
                smokingText.visibility = View.INVISIBLE
                smokingCount.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryTextView.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                contextEditText.visibility = View.VISIBLE
                smokingCountText.visibility = View.VISIBLE
                smokingCountInput.visibility = View.VISIBLE
            }
    }


    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("diary").child(selectedDay).removeValue()
    }


    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    fun saveDiary() {
        val memo=MemoData()
        memo.text=contextEditText.text.toString()
        memo.smokingCount=smokingCountInput.text.toString().toInt()
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseUtils.getUid())
            .child("diary").child(selectedDay).setValue(memo)
    }
}

