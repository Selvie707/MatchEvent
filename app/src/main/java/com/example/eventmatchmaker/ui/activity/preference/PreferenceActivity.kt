package com.example.eventmatchmaker.ui.activity.preference

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityPreferenceBinding
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.signup.SignupViewModel

class PreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferenceBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("NAME_KEY") ?: ""
        val email = intent.getStringExtra("EMAIL_KEY") ?: ""
        val password = intent.getStringExtra("PASSWORD_KEY") ?: ""

        binding.btnNext.setOnClickListener {
            viewModel.register(name, email, password, "adventure",
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

        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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