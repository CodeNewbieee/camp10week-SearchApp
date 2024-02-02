package com.example.imagesearchapp.ViewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesearchapp.RecyclerView.OnFavoriteChangeListener
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.SharedPreferences.App
import kotlinx.coroutines.launch
import okio.IOException

private val TAG = "ImageViewModel"
class ImageViewModel(val repository : SearchImageRepository = SearchImageRepository()): ViewModel() {
    var favoriteListener : OnFavoriteChangeListener? = null

    private val _searchedImage = MutableLiveData<List<Document>>()
    val searchedImage : LiveData<List<Document>> get() = _searchedImage

    private var _favoriteList = MutableLiveData<MutableList<Document>>()
    val favoriteList : LiveData<MutableList<Document>> get() = _favoriteList

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun fecthSearchImage(search : String) {
        viewModelScope.launch {
            runCatching {
                // 실제 필요한 코드는 아래 2줄, 서버와 데이터 통신하는 코드
                val image = repository.getSearchImage(search)
                _searchedImage.value = image
            }.onFailure {
                Log.e(TAG, "fecthSearchImage() failed! : ${it.message}")
                handleException(it)
            }
        }
    }

    // 스크롤 하단 감지 후 추가 검색
    fun nextFecthSearchImage() {
        viewModelScope.launch {
            val image = repository.getSearchImage(App.prefs.loadSearchInput().toString())
            _searchedImage.value = image
        }
    }

    // 에러코드 확인용 , Http 통신 에러 (401, 404 등)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun handleException(e : Throwable) {
        when (e) {
            is retrofit2.HttpException -> {
                val errorJsonString = e.response()?.errorBody()?.string()
                Log.e(TAG, "Http error : $errorJsonString")
            }
            is IOException -> Log.e(TAG, "NetWork error: $e")
            else -> Log.e(TAG, "Unexpected error: $e")
        }
    }

    fun addFavoriteList(item : Document) {
        if (!_favoriteList.value?.contains(item)!!) {
            _favoriteList.value?.add(item)
        }
    }

    fun removeFavoriteList(item : Document) {
        _favoriteList.value?.remove(item)
        favoriteListener?.onFavoriteRemoved(item)
    }

}