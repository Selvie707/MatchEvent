package com.example.eventmatchmaker.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.databinding.ListItemEventBinding
import com.example.eventmatchmaker.ui.activity.DetailActivity

class AdapterEvent :
    PagingDataAdapter<DataItem, AdapterEvent.MyViewHolder>(DIFF_CALLBACK) {

    private var eventList: List<DataItem> = emptyList()
    fun updateEvents(events: List<DataItem>) {
        eventList = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class MyViewHolder(private val binding: ListItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val user = getItem(position)
                        val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                            if (user != null) {
                                putExtra(DetailActivity.USER_ID, user.id)
                                putExtra(DetailActivity.USERNAME, user.name)
                                putExtra(DetailActivity.DESCRIPTION, user.description)
                                putExtra(DetailActivity.PICTURE, user.imageUrl)
                            }
                        }
                        itemView.context.startActivity(intent)
                    }
                }
            }

        fun bind(story: DataItem) {
            binding.tvEventName.text = story.name
            binding.tvEventDate.text = story.description
            Glide.with(binding.root.context).load(story.imageUrl).into(binding.ivEvent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
