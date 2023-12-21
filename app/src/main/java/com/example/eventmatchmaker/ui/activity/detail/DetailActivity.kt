package com.example.eventmatchmaker.ui.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.util.Pair
import androidx.core.util.component1
import androidx.core.util.component2
import com.bumptech.glide.Glide
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityDetailBinding
import com.example.eventmatchmaker.ui.activity.groupChat.GroupChatFragment

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val USER_ID = "id"
        const val EVENT_NAME = "event name"
        const val DESCRIPTION = "description"
        const val PICTURE = "picture"
        const val LOCATION = "location"
        const val START_TIME = "start_time"
        const val END_TIME = "end_time"
        const val TICKET_PRICE = "ticket_price"
        const val DRESS_CODE = "dress_code"
        const val AGE_RESTRICTION = "age_restriction"

        var idUser: String = ""
        var username: String = ""
        var description: String? = null
        var picture: String? = null
        var location: String? = null
        var start_time: String = ""
        var end_time: String = ""
        var ticket_price: String? = null
        var dress_code: String? = null
        var age_restriction: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabBack.setOnClickListener {
            finish()
        }

        val fragmentManager = supportFragmentManager
        val groupChatFragment = fragmentManager.findFragmentByTag(GroupChatFragment::class.java.simpleName)

        // Hide the GroupChatFragment if it's found initially
        if (groupChatFragment != null) {
            binding.viewCloseFragment.visibility = View.GONE
            fragmentManager.beginTransaction().hide(groupChatFragment).commit()
        }

        binding.viewCloseFragment.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag(GroupChatFragment::class.java.simpleName)
            if (fragment != null && !fragment.isHidden) {
                binding.viewCloseFragment.visibility = View.GONE
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }
        }

        binding.fabGroupChat.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val groupChatFragment = fragmentManager.findFragmentByTag(GroupChatFragment::class.java.simpleName)

            if (groupChatFragment != null && groupChatFragment.isHidden) {
                binding.viewCloseFragment.visibility = View.VISIBLE
                fragmentManager.beginTransaction().show(groupChatFragment).commit()
            } else {
                // Add the GroupChatFragment if it's not already added
                val homeFragment = GroupChatFragment()
                binding.viewCloseFragment.visibility = View.VISIBLE
                fragmentManager.beginTransaction()
                    .add(R.id.flGroupChat, homeFragment, GroupChatFragment::class.java.simpleName)
                    .commit()
            }
        }

        idUser = intent.getStringExtra(USER_ID) ?: "none"
        username = intent.getStringExtra(EVENT_NAME) ?: "none"
        description = intent.getStringExtra(DESCRIPTION) ?: "none"
        picture = intent.getStringExtra(PICTURE) ?: "none"
        location = intent.getStringExtra(LOCATION) ?: "none"
        start_time = intent.getStringExtra(START_TIME) ?: "TZ"
        end_time = intent.getStringExtra(END_TIME) ?: "TZ"
        ticket_price = intent.getStringExtra(TICKET_PRICE) ?: "none"
        dress_code = intent.getStringExtra(DRESS_CODE) ?: "none"
        age_restriction = intent.getStringExtra(AGE_RESTRICTION) ?: "none"

        val (startDate, startTime) = separateDateTime(start_time)
        val (endDate, endTime) = separateDateTime(end_time)

        binding.tvEventName.text = username
        binding.tvEventAbout.text = description
//        binding.tvDate.text = "Tanggal guys"
        binding.tvDate.text = "$startDate - $endDate, $startTime - $endTime"
        binding.tvTicketPrice.text = ticket_price
        binding.tvEventDressCode.text = dress_code
        binding.tvEventAge.text = age_restriction

        Glide.with(this)
            .load(picture)
            .into(binding.ivEvent)
    }

    private fun separateDateTime(dateTimeString: String): Pair<String, String> {
        val dateTimeParts = dateTimeString.split("T", "Z") // Splitting the string by 'T' and 'Z' to get date and time
        val date = dateTimeParts[0] // Date part
        val time = dateTimeParts[1].substring(0, 5) // Time part (considering the format is 'HH:mm:ss.SSS')

        return Pair(date, time)
    }
}