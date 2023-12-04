package com.example.eventmatchmaker.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        binding.btnAddEvent.setOnClickListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
//        if (v?.id == R.id.btnAddEvent) {
//            val categoryFragment = CategoryFragment()
//            val fragmentManager = parentFragmentManager
//            fragmentManager.beginTransaction().apply {
//                replace(R.id.frame_container, categoryFragment, CategoryFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
//        }
    }
}