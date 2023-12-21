package com.example.eventmatchmaker.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityProfileBinding
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.addEvent.AddEventActivity
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.onboarding.OnboardingActivity
import com.example.eventmatchmaker.ui.activity.search.SearchActivity

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var token: String
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            user.token.let {
                token = it

                binding.tvName.text = user.name

                if (!::token.isInitialized) {
                    token = it
                }
            }
        }

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
//                    binding.navView.selectedItemId = R.id.navigation_home
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
//                    binding.navView.selectedItemId = R.id.navigation_search
                    true
                }
                else -> false
            }
        }

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