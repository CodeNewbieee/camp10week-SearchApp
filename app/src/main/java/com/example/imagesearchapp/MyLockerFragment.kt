package com.example.imagesearchapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.databinding.FragmentMyLockerBinding


class MyLockerFragment : Fragment() {

    private var _binding: FragmentMyLockerBinding? = null
    private val binding get() = _binding!!
    private val searchListAdapter by lazy { SearchListAdapter() }
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
            rvFragMylocker.adapter = searchListAdapter.apply {
                itemClick = object : SearchListAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        // 보관함 아이템 클릭시 내역 삭제
                        searchListAdapter.apply {
                            searchResult.removeAt(position)
                            searchResult[position].isLiked = false
                            notifyDataSetChanged()
                        }
                    }
                }
            }
            rvFragMylocker.layoutManager = GridLayoutManager(context,2)
            rvFragMylocker.setHasFixedSize(true)
            // 메인에서 보낸 객체 데이터 받고 어댑터의 리스트에 보내기
            setFragmentResultListener(Constans.REQUEST_KEY2) { requestKey, bundle ->
                val result = bundle.getParcelable<Document>(Constans.FAVORITE_DATA2)
                result?.let { searchListAdapter.searchResult.add(it) }
                searchListAdapter.notifyDataSetChanged()
                Log.d("click data", "MyLockerFragment list: $result")
            }
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