package com.example.eventmatchmaker.ui.activity.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatchmaker.databinding.ActivityRecommendBinding
import com.example.eventmatchmaker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}