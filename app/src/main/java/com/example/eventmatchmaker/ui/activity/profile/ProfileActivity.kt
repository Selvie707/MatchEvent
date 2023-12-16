package com.example.eventmatchmaker.ui.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.eventmatchmaker.databinding.ActivityProfileBinding
import com.example.eventmatchmaker.databinding.ActivityRecommendBinding
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.addEvent.AddEventActivity
import com.example.eventmatchmaker.ui.activity.main.MainViewModel
import com.example.eventmatchmaker.ui.activity.onboarding.OnboardingActivity

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddEvent.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }

        binding.btnLogOut.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }
    }
}