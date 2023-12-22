package com.example.eventmatchmaker.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.data.response.DataItemCategories
import com.example.eventmatchmaker.databinding.ListItemPreferenceBinding
import com.example.eventmatchmaker.ui.activity.detail.DetailActivity

class AdapterPreferences: PagingDataAdapter<DataItemCategories, AdapterPreferences.MyViewHolder>(DIFF_CALLBACK) {

    private var eventList: List<DataItemCategories> = emptyList()
    fun updateEvents(events: List<DataItemCategories>) {
        eventList = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemPreferenceBinding.inflate(
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

    inner class MyViewHolder(private val binding: ListItemPreferenceBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    // TODO Pass the Id and the name of the item that clicked

                    val resolvedWhiteColor = ContextCompat.getColor(itemView.context, R.color.white)
                    val resolvedBlackColor = ContextCompat.getColor(itemView.context, R.color.black)
                    val resolvedBackground = ContextCompat.getColor(itemView.context, R.color.dark_blue)

                    if (binding.tvPreferenceCategory.currentTextColor == resolvedWhiteColor) {
                        binding.tvPreferenceCategory.setBackgroundColor(resolvedWhiteColor)
                        binding.tvPreferenceCategory.setTextColor(resolvedBlackColor)
                    } else {
                        binding.tvPreferenceCategory.setBackgroundColor(resolvedBackground)
                        binding.tvPreferenceCategory.setTextColor(resolvedWhiteColor)
                    }

                    // TODO clean this class' code
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        val user = getItem(position)
//                        val intent = Intent(itemView.context, DetailActivity::class.java).apply {
//                            if (user != null) {
//                                putExtra(DetailActivity.USER_ID, user.id)
//                                putExtra(DetailActivity.EVENT_NAME, user.name)
//                            }
//                        }
//                        itemView.context.startActivity(intent)
//                    }
                }
            }

        fun bind(categories: DataItemCategories) {
            binding.tvPreferenceCategory.text = categories.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemCategories>() {
            override fun areItemsTheSame(oldItem: DataItemCategories, newItem: DataItemCategories): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItemCategories, newItem: DataItemCategories): Boolean {
                return oldItem == newItem
            }
        }
    }
}
