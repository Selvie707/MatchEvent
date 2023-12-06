package com.example.eventmatchmaker.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.FragmentProfileBinding
import com.example.eventmatchmaker.ui.AddEventActivity
import com.example.eventmatchmaker.ui.edit_profile.EditProfileActivity
import com.example.eventmatchmaker.ui.onboarding.OnboardingActivity

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnAddEvent.setOnClickListener(this)
        binding.btnEditProfile.setOnClickListener(this)
        binding.btnLogOut.setOnClickListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddEvent -> {
                val intent = Intent(activity, AddEventActivity::class.java)
                startActivity(intent)
            }
            R.id.btnEditProfile -> {
                val intent = Intent(activity, EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.btnLogOut -> {
                val intent = Intent(activity, OnboardingActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}