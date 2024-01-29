package com.example.imagesearchapp

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.Retrofit.SearchData
import com.example.imagesearchapp.Retrofit.SearchRetrofit
import com.example.imagesearchapp.SharedPreferences.App
import com.example.imagesearchapp.SharedPreferences.SharedPreferencesManager
import com.example.imagesearchapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

//    private val searchResult = MutableLiveData<List<Document>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnFragSearch.setOnClickListener { // 키보드 내리기
                val keyboardHidden = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboardHidden.hideSoftInputFromWindow(etFragInput.windowToken,0)

                rvFragSearchlist.adapter = imageAdapter
                rvFragSearchlist.layoutManager = GridLayoutManager(context,2)
                rvFragSearchlist.setHasFixedSize(true)

                // 검색창에 입력한 값의 결과 불러오기
                fecthSearchImage(etFragInput.text.toString())
            }
            // 저장된 검색어 불러오기
            etFragInput.setText(App.prefs.loadSearchInput())


        }
    }

    private fun fecthSearchImage(search : String) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val image = getSearchImage(search)
                if(imageAdapter.searchResult.isNullOrEmpty())
                    imageAdapter.searchResult.addAll(image)
                else {
                    imageAdapter.searchResult.clear()
                    imageAdapter.searchResult.addAll(image)
                }
                // 동적데이터로 livedata나 listadapter를 사용한것이 아니라서 notify해줘야 recyclerview에 반영
                imageAdapter.notifyDataSetChanged()
            }.onFailure {
                Log.e("KakaoApi", "fecthSearchImage() failed! : ${it.message}")
            }

        }
    }

    private suspend fun getSearchImage(search : String) = withContext(Dispatchers.IO) {
        SearchRetrofit.api.getSearchImage(query = search).documents
    }

    override fun onStop() {
        super.onStop()
        // 검색어 저장
        App.prefs.saveSearchInput(binding.etFragInput.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}