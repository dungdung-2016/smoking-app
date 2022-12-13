package com.example.ex2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ex2.databinding.ActivityNaviBinding



private const val TAG_CALENDER = "calender_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setFragment(TAG_HOME, Fragment1())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.goal_fragment -> setFragment(TAG_CALENDER, Fragment2())
                R.id.home_fragment -> setFragment(TAG_HOME, Fragment1())
                R.id.ranking_fragment-> setFragment(TAG_MY_PAGE, Fragment3())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val calender = manager.findFragmentByTag(TAG_CALENDER)
        val home = manager.findFragmentByTag(TAG_HOME)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        if (calender != null){
            fragTransaction.hide(calender)
        }

        if (home != null){
            fragTransaction.hide(home)
        }

        if (myPage != null) {
            fragTransaction.hide(myPage)
        }

        if (tag == TAG_CALENDER) {
            if (calender!=null){
                fragTransaction.show(calender)
            }
        }
        else if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_MY_PAGE){
            if (myPage != null){
                fragTransaction.show(myPage)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}