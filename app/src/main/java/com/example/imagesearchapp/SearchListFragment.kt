package com.example.imagesearchapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.Retrofit.SearchRetrofit
import com.example.imagesearchapp.SharedPreferences.App
import com.example.imagesearchapp.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface OnFavoriteChangeListener {
    fun onFavoriteRemoved(item:Document)
}

class SearchListFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnFragSearch.setOnClickListener { // 키보드 내리기
                val keyboardHidden = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboardHidden.hideSoftInputFromWindow(etFragInput.windowToken,0)

                rvFragSearchlist.adapter = searchListAdapter.apply {
                    itemClick = object :SearchListAdapter.ItemClick{ // 검색창에서 특정리스트 아이템 클릭시 MainActity 리스트 변수에 갹체 데이터 전달
                        override fun onClick(view: View, position: Int) {
                            searchList[position].isLiked = true
                            notifyDataSetChanged()
                            (activity as? MainActivity)?.addFavoriteList(searchList[position])
                        }
                    }
                }
                rvFragSearchlist.layoutManager = GridLayoutManager(context,2)
                rvFragSearchlist.setHasFixedSize(true)

                // 같은 아이템 favorite 삭제
                (activity as? MainActivity)?.favoriteListener = object  : OnFavoriteChangeListener {
                    override fun onFavoriteRemoved(item: Document) {
                        searchListAdapter.searchList.find { it == item }?.isLiked = false
                        searchListAdapter.notifyDataSetChanged()
                    }
                }

                // 검색창에 입력한 값의 결과 불러오기
                if(etFragInput.text.isEmpty()) Snackbar.make(root,"검색어를 입력해주세요",2000).show()
                else fecthSearchImage(etFragInput.text.toString())
            }
            // 저장된 검색어 불러오기
            etFragInput.setText(App.prefs.loadSearchInput())
        }
    }

    private fun fecthSearchImage(search : String) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val image = getSearchImage(search)
                if(searchListAdapter.searchList.isNullOrEmpty()){
                    searchListAdapter.searchList.addAll(image)
                } else {
                    searchListAdapter.searchList.clear()
                    searchListAdapter.searchList.addAll(image)
                }
                // 동적데이터로 livedata나 listadapter를 사용한것이 아니라서 notify해줘야 recyclerview에 반영
                searchListAdapter.notifyDataSetChanged()
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

    override fun onDetach() {
        super.onDetach()
        //main에 붙은 메모리 콜백을 해제 (메모리누수 방지, 콜백 지속 방지)
        (activity as? MainActivity)?.favoriteListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}