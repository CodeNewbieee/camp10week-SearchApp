package com.example.imagesearchapp.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imagesearchapp.MyLockerFragment
import com.example.imagesearchapp.SearchFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val searchFragment = SearchFragment()
    private val myLockerFragment = MyLockerFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> searchFragment
            1 -> myLockerFragment
            else -> throw IllegalStateException("Invaild Position : ${position}")
        }
    }
}