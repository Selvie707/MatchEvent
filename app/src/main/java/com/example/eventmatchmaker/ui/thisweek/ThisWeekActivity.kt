package com.example.eventmatchmaker.ui.thisweek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityThisWeekBinding

class ThisWeekActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThisWeekBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThisWeekBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}