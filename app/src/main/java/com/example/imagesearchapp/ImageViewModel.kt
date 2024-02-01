package com.example.imagesearchapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagesearchapp.Retrofit.Document
import kotlinx.coroutines.launch
import okio.IOException

private val TAG = "ImageViewModel"
class ImageViewModel(val repository : SearchImageRepository = SearchImageRepository()): ViewModel() {
    private val _searchedImage = MutableLiveData<List<Document>>()
    val searchedImage : LiveData<List<Document>> get() = _searchedImage

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

    // 에러코드 확인용 , Http 통신 에러 (401, 404 등)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun handleException(e : Throwable) {
        when (e) {
            is retrofit2.HttpException -> {
                val errorJsonString = e.response()?.errorBody()?.string()
                Log.e(TAG, "Http error : $errorJsonString" )
            }
            is IOException -> Log.e(TAG, "NetWork error: $e")
            else -> Log.e(TAG, "Unexpected error: $e")
        }
    }

}