package com.example.imagesearchapp.RecyclerView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapp.MainActivity
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
                        myLockerListAdapter.apply {
                          (activity as? MainActivity)?.removeFavoriteList(myLockerList[position])
                            notifyDataSetChanged()
                        }
                    }
                }
            }
            myLockerListAdapter.myLockerList = (activity as? MainActivity)?.favoriteList ?: mutableListOf()
            rvFragMylocker.layoutManager = GridLayoutManager(context,2)
            rvFragMylocker.setHasFixedSize(true)
        }
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