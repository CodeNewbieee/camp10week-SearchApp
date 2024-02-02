package com.example.imagesearchapp.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapp.Retrofit.Document
import com.example.imagesearchapp.databinding.ProgressbarBinding
import com.example.imagesearchapp.databinding.SearchResultListBinding
import java.text.SimpleDateFormat

class SearchListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_SEARCHLIST = 0
        private const val TYPE_LOADING = 1
    }

    var searchList = mutableListOf<Document>()

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_SEARCHLIST -> ImageViewHolder(SearchResultListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            else -> LoadingHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).bind(searchList[position])
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it,position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(searchList.get(position)) {
            null -> TYPE_LOADING
            else -> TYPE_SEARCHLIST
        }
    }

    override fun getItemCount(): Int {
        return if(searchList == null) 0 else searchList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ImageViewHolder(private val binding : SearchResultListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Document) {
            with(binding) {
                Glide.with(itemView).load(item.thumbnailUrl).into(ivSearchThumbnail)
                tvSearchSitename.text = item.displaySiteName
                tvSearchDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.datetime)
                if(item.isLiked) {
                    ivSearchFavorite.visibility = View.VISIBLE
                }
                else {
                    ivSearchFavorite.visibility = View.GONE
                }
            }
        }
    }
    inner class LoadingHolder(private val binding: ProgressbarBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    fun updateList (list : List<Document>) {
        searchList.clear()
        searchList.addAll(list)
        notifyDataSetChanged()
    }

    fun nextupdateList (List : List<Document>) {
        searchList.addAll(List)
        notifyDataSetChanged()

    }
}