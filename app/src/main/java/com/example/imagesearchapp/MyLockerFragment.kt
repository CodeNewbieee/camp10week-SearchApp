package com.example.imagesearchapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.databinding.FragmentMyLockerBinding
import com.example.imagesearchapp.databinding.FragmentSearchBinding


class MyLockerFragment : Fragment() {

    private var _binding: FragmentMyLockerBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter() }
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
            rvFragMylocker.adapter = imageAdapter.apply {
                itemClick = object : ImageAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {

                        imageAdapter.notifyDataSetChanged()
                    }
                }
            }
            setFragmentResultListener(Constans.REQUEST_KEY2) { requestKey, bundle ->
                val result = bundle.getParcelable<Document>(Constans.FAVORITE_DATA2)
                result?.let { imageAdapter.searchResult.add(it) }
                Log.d("data", "my locker onViewCreated: $result")
            }

//            val data = arguments?.getParcelableArrayList<Document>(Constans.FAVORITE_DATA2)
//            Log.d("data", "mylockerfrag data : $data ")
//            data?.let { imageAdapter.searchResult.addAll(it) }
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