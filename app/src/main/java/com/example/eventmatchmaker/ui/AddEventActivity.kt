package com.example.eventmatchmaker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityAddEventBinding

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddEvent.setOnClickListener {
            finish()
        }
    }
}