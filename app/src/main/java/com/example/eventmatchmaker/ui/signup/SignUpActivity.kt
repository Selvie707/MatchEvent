package com.example.eventmatchmaker.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivitySignUpBinding
import com.example.eventmatchmaker.ui.login.LoginActivity
import com.example.eventmatchmaker.ui.preference.PreferenceActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamera.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnSignUpSignUp.setOnClickListener {
            startActivity(Intent(this, PreferenceActivity::class.java))
        }

        binding.tvHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}