package com.example.imagesearchapp.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imagesearchapp.RecyclerView.MyLockerFragment
import com.example.imagesearchapp.RecyclerView.SearchListFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val searchListFragment = SearchListFragment()
    private val myLockerFragment = MyLockerFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> searchListFragment
            1 -> myLockerFragment
            else -> throw IllegalStateException("Invaild Position : ${position}")
        }
    }
}