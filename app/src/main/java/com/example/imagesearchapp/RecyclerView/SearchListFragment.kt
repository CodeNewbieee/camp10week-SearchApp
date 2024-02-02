package com.example.imagesearchapp.RecyclerView

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapp.ViewModel.ImageViewModel
import com.example.imagesearchapp.MainActivity
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.SharedPreferences.App
import com.example.imagesearchapp.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar

interface OnFavoriteChangeListener {
    fun onFavoriteRemoved(item:Document)
}

class SearchListFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val searchViewModel by activityViewModels<ImageViewModel>()

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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            btnFragSearch.setOnClickListener {
                // 검색어 저장
                if(etFragInput.text.isNotEmpty()) {
                    App.prefs.saveSearchInput(etFragInput.text.toString())
                }
                // 키보드 내리기
                val keyboardHidden = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboardHidden.hideSoftInputFromWindow(etFragInput.windowToken,0)

                // recyclerview 구성, 아이템클릭 콜백함수
                rvFragSearchlist.layoutManager = GridLayoutManager(context,2)
                rvFragSearchlist.setHasFixedSize(true)
                rvFragSearchlist.adapter = searchListAdapter.apply {
                    itemClick = object : SearchListAdapter.ItemClick {
                        override fun onClick(view: View, position: Int) {
                            // 검색창에서 특정리스트 아이템 클릭시 MainActity 리스트 변수에 갹체 데이터 전달
                            (activity as? MainActivity)?.addFavoriteList(searchList[position])
                            searchList[position].isLiked = true
                            notifyDataSetChanged()
                            Snackbar.make(root,"선택된 이미지가 보관되었습니다.",1500).show()
                            // 검색된 리스트 아이템 클릭시 shared에 저장
                            App.prefs.saveMyLockerList((activity as? MainActivity)?.favoriteList ?: mutableListOf())
                        }
                    }
                }

                // 같은 아이템 favorite 삭제
                (activity as? MainActivity)?.favoriteListener = object  : OnFavoriteChangeListener {
                    override fun onFavoriteRemoved(item: Document) {
                        searchListAdapter.searchList.find { it == item }?.isLiked = false
                        searchListAdapter.notifyDataSetChanged()
                    }
                }

                // 검색창에 입력한 값의 결과 불러오기
                if(etFragInput.text.isEmpty()) {
                    Snackbar.make(root,"검색어를 입력해주세요",2000).show()
                }
                else {
                    searchViewModel.fecthSearchImage(etFragInput.text.toString())
                    searchViewModel.searchedImage.observe(viewLifecycleOwner){list->
                        searchListAdapter.updateList(list)
                    }
                }
            }

            val fadeIn = AlphaAnimation(0f,1f).apply { duration = 200 } // 서서히 나오기 , f는 투명도
            val fadeOut = AlphaAnimation(1f,0f).apply { duration = 200 } // 서서히 사라지기, f는 투명도
            var isTop = true

            // 를로팅 버튼 구현 (최상단 이동)
            rvFragSearchlist.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if(!rvFragSearchlist.canScrollVertically(-1)&& newState==RecyclerView.SCROLL_STATE_IDLE){ // 스크롤이 최상단인 상태면서 스크롤을 하지 않은 상태일때
                        btnSearchFloating.startAnimation(fadeOut)
                        btnSearchFloating.visibility = View.GONE
                        isTop=true
                    } else if(isTop) {
                        btnSearchFloating.visibility = View.VISIBLE
                        btnSearchFloating.startAnimation(fadeIn)
                        isTop=false
                    }
                }
            })
            btnSearchFloating.setOnClickListener {
                rvFragSearchlist.smoothScrollToPosition(0)
            }

            //무한 스크롤을 위한 리사이클러뷰 위치 감지
            rvFragSearchlist.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition = (rvFragSearchlist.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = rvFragSearchlist.adapter?.itemCount?.minus(1)
                    // 뷰가 가장 하단일때 (더이상 뿌릴 아이템이 없을떄)
                    if(rvFragSearchlist.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                        searchViewModel.searchedImage.observe(viewLifecycleOwner) {
                            searchViewModel.nextFecthSearchImage()
                            searchListAdapter.nextupdateList(it)
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        // 저장된 검색어 불러오기
        binding.etFragInput.setText(App.prefs.loadSearchInput())
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