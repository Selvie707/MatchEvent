package com.example.eventmatchmaker.ui.activity.preference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityPreferenceBinding
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.login.LoginActivity
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.main.MainViewModel
import com.example.eventmatchmaker.ui.activity.signup.SignupViewModel
import com.example.eventmatchmaker.ui.adapter.AdapterPreferences
import com.example.eventmatchmaker.ui.adapter.LoadingStateAdapter

class PreferenceActivity : AppCompatActivity() {
    // TODO show categories adapter using recyclerview
    // TODO when category clicked, change the color and save the data

    private val viewModelPreferences by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPreferenceBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        val layoutManagerRecommendation = GridLayoutManager(this, 2)

        binding.rvItem.layoutManager = layoutManagerRecommendation
        binding.rvItem.addItemDecoration(DividerItemDecoration(this, layoutManagerRecommendation.orientation))

        val categoryAdapter = AdapterPreferences()

        binding.rvItem.adapter = categoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                categoryAdapter.retry()
            }
        )

        viewModelPreferences.preferences.observe(this) {
            categoryAdapter.submitData(lifecycle, it)
        }

        val name = intent.getStringExtra("NAME_KEY") ?: ""
        val email = intent.getStringExtra("EMAIL_KEY") ?: ""
        val password = intent.getStringExtra("PASSWORD_KEY") ?: ""

        // TODO fix sign up preferences features

        binding.btnNext.setOnClickListener {
            viewModel.register(email, password, name, "adventure",
                {
                    showRegistrationSuccessDialog(name)
                },
                { errorMessage ->
                    showRegistrationErrorDialog(errorMessage)
                }
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showRegistrationSuccessDialog(name: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this).apply {
                val dialogMessage = resources.getString(R.string.welcome) + name

                setTitle(resources.getString(R.string.dialogRegisterSuccess))
                setMessage(dialogMessage)
                setPositiveButton(resources.getString(R.string.oke)) { _, _ ->
                    startActivity(Intent(this@PreferenceActivity, LoginActivity::class.java))
                    finish()
                }
                create().show()
            }
        }
    }

    private fun showRegistrationErrorDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this).apply {
                setTitle(resources.getString(R.string.dialogRegisterFailed))
                setMessage(message)
                setPositiveButton(resources.getString(R.string.oke), null)
                create().show()
            }
        }
    }
}