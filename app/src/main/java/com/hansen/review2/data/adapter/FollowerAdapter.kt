package com.hansen.review2.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hansen.review2.data.response.FollowersResponseItem
import com.hansen.review2.databinding.ItemReviewBinding

class FollowerAdapter: ListAdapter<FollowersResponseItem, FollowerAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FollowersResponseItem){
            Glide.with(binding.root.context)
                .load(item.avatarUrl)
                .into(binding.imgAvatar)
            binding.tvItem.text = item.login
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}