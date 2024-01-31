package com.example.imagesearchapp.Retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date

data class SearchData(
    val documents: List<Document>,
    val meta: Meta
)

data class Meta(
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)
@Parcelize
data class Document(
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("display_sitename")
    val displaySiteName: String,
    val datetime: Date,
    var isLiked: Boolean = false
) : Parcelable