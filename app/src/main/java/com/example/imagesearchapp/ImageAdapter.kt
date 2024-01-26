package com.example.imagesearchapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.Retrofit.SearchData
import com.example.imagesearchapp.databinding.SearchResultListBinding

class ImageAdapter(private val searchResult : List<Document>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(SearchResultListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).bind(searchResult[position])
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    inner class ImageViewHolder(private val binding : SearchResultListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Document) {
            with(binding) {
                tvSearchlistTitle.text = item.displaySiteName
                tvSearchlistDate.text = item.datetime
            }
        }
    }
}