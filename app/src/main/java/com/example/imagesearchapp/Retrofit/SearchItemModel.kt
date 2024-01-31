package com.example.imagesearchapp.Retrofit

import java.util.Date

data class SearchItemModel(
    var title : String,
    var dateTime : Date,
    var thumbnail : String,
    var isLiked: Boolean = false
    )