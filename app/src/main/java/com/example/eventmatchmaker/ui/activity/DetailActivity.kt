package com.example.eventmatchmaker.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.add
import com.bumptech.glide.Glide
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityDetailBinding
import com.example.eventmatchmaker.ui.activity.groupChat.GroupChatFragment

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val USER_ID = "id"
        const val USERNAME = "username"
        const val DESCRIPTION = "description"
        const val PICTURE = "picture"

        var idUser: String = ""
        var username: String = ""
        var description: String? = null
        var picture: String? = null
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
            fragmentManager.beginTransaction().hide(groupChatFragment).commit()
        }

        binding.ivEvent.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag(GroupChatFragment::class.java.simpleName)
            if (fragment != null && !fragment.isHidden) {
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }
        }

        binding.fabGroupChat.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val groupChatFragment = fragmentManager.findFragmentByTag(GroupChatFragment::class.java.simpleName)

            if (groupChatFragment != null && groupChatFragment.isHidden) {
                fragmentManager.beginTransaction().show(groupChatFragment).commit()
            } else {
                // Add the GroupChatFragment if it's not already added
                val homeFragment = GroupChatFragment()
                fragmentManager.beginTransaction()
                    .add(R.id.flGroupChat, homeFragment, GroupChatFragment::class.java.simpleName)
                    .commit()
            }
        }

        idUser = intent.getStringExtra(USER_ID) ?: ""
        username = intent.getStringExtra(USERNAME) ?: ""
        description = intent.getStringExtra(DESCRIPTION) ?: ""
        picture = intent.getStringExtra(PICTURE) ?: ""

        binding.tvEventName.text = username
        binding.tvEventAbout.text = description

        Glide.with(this)
            .load(picture)
            .into(binding.ivEvent)
    }
}