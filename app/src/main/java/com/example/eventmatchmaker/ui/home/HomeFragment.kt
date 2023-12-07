package com.example.eventmatchmaker.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.FragmentHomeBinding
import com.example.eventmatchmaker.ui.recommended.RecommendActivity
import com.example.eventmatchmaker.ui.thisweek.ThisWeekActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvMoreRecommended.setOnClickListener(this)
        binding.tvMoreThisWeek.setOnClickListener(this)

//        val textView: TextView = binding.tvHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvMoreRecommended -> {
                val intent = Intent(activity, RecommendActivity::class.java)
                startActivity(intent)
            }
            R.id.tvMoreThisWeek -> {
                val intent = Intent(activity, ThisWeekActivity::class.java)
                startActivity(intent)
            }
        }
    }
}