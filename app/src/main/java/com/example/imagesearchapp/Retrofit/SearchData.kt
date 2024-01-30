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
    val collection: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val width: Int,
    val height: Int,
    @SerializedName("display_sitename")
    val displaySiteName: String,
    @SerializedName("doc_url")
    val docUrl: String,
    val datetime: Date,
    var isLiked: Boolean
) : Parcelable