package com.example.imagesearchapp.ViewModel

import com.example.imagesearchapp.Retrofit.SearchRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchImageRepository {
     suspend fun getSearchImage(search : String) = withContext(Dispatchers.IO) {
         SearchRetrofit.api.getSearchImage(query = search).documents
     }
}