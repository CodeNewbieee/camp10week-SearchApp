package com.example.imagesearchapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.databinding.SearchResultListBinding
import java.text.SimpleDateFormat

class ImageAdapter() : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    val searchResult = mutableListOf<Document>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ImageViewHolder {
        return ImageViewHolder(SearchResultListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(searchResult[position])
    }
    override fun getItemCount(): Int {
        return searchResult.size
    }

    inner class ImageViewHolder(private val binding : SearchResultListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Document) {
            with(binding) {
                Glide.with(itemView).load(item.thumbnailUrl).into(ivSearchThumbnail)
                tvSearchSitename.text = item.displaySiteName
                tvSearchDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.datetime)
            }
        }
    }
}