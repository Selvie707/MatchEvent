package com.example.eventmatchmaker.ui.activity.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivitySearchBinding
import com.example.eventmatchmaker.ui.activity.FiltersActivity
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.profile.ProfileActivity
import com.example.eventmatchmaker.ui.adapter.AdapterEvent
import com.example.eventmatchmaker.ui.adapter.LoadingStateAdapter

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySearchBinding
    private var name = ""
    private var category = ""
    private var ageLimit = ""
    private var priceStart = ""
    private var priceEnd = ""
    private var startTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManagerRecommendation = GridLayoutManager(this, 2)

        binding.rvSearchEvent.layoutManager = layoutManagerRecommendation
        binding.rvSearchEvent.addItemDecoration(DividerItemDecoration(this, layoutManagerRecommendation.orientation))

        val adapter = AdapterEvent()

        binding.rvSearchEvent.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getUserStories(
            name,
            category,
            ageLimit,
            priceStart,
            priceEnd,
            startTime
        ).observe(this@SearchActivity) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

//        viewModel.story.observe(this) {
//            adapter.submitData(lifecycle, it)
//        }

        //

        with(binding) {
            svSearch.setupWithSearchBar(sbSearch)
            svSearch
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    svSearch.hide()
                    viewModel.getUserStories(
                        svSearch.text.toString(),
                        "",
                        "",
                        "",
                        "",
                        ""
                    ).observe(this@SearchActivity) { pagingData ->
                        adapter.submitData(lifecycle, pagingData)
                    }
                    false
                }
        }

        //

        binding.ivFilter.setOnClickListener {
            startActivity(Intent(this, FiltersActivity::class.java))
        }

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}