package com.example.eventmatchmaker.ui.activity.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivitySignUpBinding
import com.example.eventmatchmaker.ui.activity.login.LoginActivity
import com.example.eventmatchmaker.ui.activity.preference.PreferenceActivity

class SignUpActivity : AppCompatActivity() {
    // TODO fix repeat password feature
    // TODO [clean code]

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pbProgressBar.visibility = View.GONE

        setupUI()
        setUpViewModel()
        setUpTextWatcher()
        setMyButtonEnable()
        signUpButtonOnClick()

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.pbProgressBar.visibility = View.VISIBLE
            } else {
                binding.pbProgressBar.visibility = View.GONE
            }
        }

        // TODO open intent to take user's photo
        binding.ivCamera.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun signUpButtonOnClick() {
        binding.btnSignUpSignUp.setOnClickListener {
            val name = binding.etLoginUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val repeatPassword = binding.etRePassword.text.toString()

            if (password == repeatPassword) {
                val intent = Intent(this, PreferenceActivity::class.java)
                intent.putExtra("NAME_KEY", name)
                intent.putExtra("EMAIL_KEY", email)
                intent.putExtra("PASSWORD_KEY", password)
                startActivity(intent)
            } else {
                binding.etPassword.error = "Password Mismatch"
                binding.etRePassword.error = "Password Mismatch"
            }
        }
    }

    private fun setUpTextWatcher() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
    }

    private fun setMyButtonEnable() {
        val result = binding.etPassword.text
        binding.btnSignUpSignUp.isEnabled = result != null && result.toString().isNotEmpty()
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

    private fun showRegistrationSuccessDialog() {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this).apply {
                val dialogMessage = resources.getString(R.string.welcome) + ", ${binding.etEmail.text}!"

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