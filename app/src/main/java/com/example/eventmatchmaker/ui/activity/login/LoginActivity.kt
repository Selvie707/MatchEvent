package com.example.eventmatchmaker.ui.activity.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityLoginBinding
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.signup.SignUpActivity
import com.example.eventmatchmaker.ui.activity.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pbProgressBar.visibility = View.GONE

        setupUI()
        buttonLoginOnClick()

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.pbProgressBar.visibility = View.VISIBLE
            } else {
                binding.pbProgressBar.visibility = View.GONE
            }
        }

//        binding.btnLoginLogin.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }

        binding.tvNotAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun buttonLoginOnClick() {
        binding.btnLoginLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etPassword.text.toString()

            viewModel.login(email, password,
                {
                    navigateToMainActivity()
                },
                { errorMessage ->
                    showLoginErrorDialog(errorMessage)
                }
            )
        }
    }

    private fun setupUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoginErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(resources.getString(R.string.dialogLoginFailed))
            setMessage(message)
            setPositiveButton(resources.getString(R.string.oke), null)
            create().show()
        }
    }
}