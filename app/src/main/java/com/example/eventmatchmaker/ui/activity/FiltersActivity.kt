package com.example.eventmatchmaker.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityFiltersBinding

class FiltersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            finish()
        }
    }
}