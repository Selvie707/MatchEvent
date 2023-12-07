package com.example.eventmatchmaker.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.FragmentSearchBinding
import com.example.eventmatchmaker.ui.FiltersActivity
import com.example.eventmatchmaker.ui.recommended.RecommendActivity
import com.example.eventmatchmaker.ui.thisweek.ThisWeekActivity

class SearchFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.ivFilter.setOnClickListener(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivFilter -> {
                val intent = Intent(activity, FiltersActivity::class.java)
                startActivity(intent)
            }
        }
    }
}