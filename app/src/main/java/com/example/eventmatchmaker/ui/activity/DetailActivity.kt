package com.example.eventmatchmaker.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.eventmatchmaker.databinding.ActivityDetailBinding

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

        binding.fabGroupChat.setOnClickListener {
            startActivity(Intent(this, GroupChatActivity::class.java))
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