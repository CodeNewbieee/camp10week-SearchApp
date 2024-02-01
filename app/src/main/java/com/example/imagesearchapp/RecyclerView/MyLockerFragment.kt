package com.example.imagesearchapp.RecyclerView

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapp.MainActivity
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.databinding.FragmentMyLockerBinding


class MyLockerFragment : Fragment() {

    private var _binding: FragmentMyLockerBinding? = null
    private val binding get() = _binding!!
    private val myLockerListAdapter by lazy { MyLockerListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvFragMylocker.adapter = myLockerListAdapter.apply {
                itemClick = object : MyLockerListAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        // 보관함 아이템 클릭시 내역 삭제
                        (activity as? MainActivity)?.removeFavoriteList(myLockerList[position])
                        myLockerListAdapter.notifyDataSetChanged()
                    }
                }
            }
            rvFragMylocker.layoutManager = GridLayoutManager(context,2)
            rvFragMylocker.setHasFixedSize(true)

            val fadeIn = AlphaAnimation(0f,1f).apply { duration = 200 } // 서서히 나오기 , f는 투명도
            val fadeOut = AlphaAnimation(1f,0f).apply { duration = 200 } // 서서히 사라지기, f는 투명도
            var isTop = true

            // 를로팅 버튼 구현 (최상단 이동)
            rvFragMylocker.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(!rvFragMylocker.canScrollVertically(-1)&& newState== RecyclerView.SCROLL_STATE_IDLE){ // 스크롤이 최상단인 상태면서 스크롤을 하지 않은 상태일때
                        btnLockerFloating.startAnimation(fadeOut)
                        btnLockerFloating.visibility = View.GONE
                        isTop=true
                    } else if(isTop) {
                        btnLockerFloating.visibility = View.VISIBLE
                        btnLockerFloating.startAnimation(fadeIn)
                        isTop=false
                    }
                }
            })
            btnLockerFloating.setOnClickListener {
                rvFragMylocker.smoothScrollToPosition(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myLockerListAdapter.myLockerList = (activity as? MainActivity)?.favoriteList ?: mutableListOf()
        myLockerListAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(param1: String) =
            MyLockerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}