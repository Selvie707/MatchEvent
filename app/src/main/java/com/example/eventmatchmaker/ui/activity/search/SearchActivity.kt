package com.example.eventmatchmaker.ui.activity.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivitySearchBinding
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.filter.FiltersActivity
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.activity.profile.ProfileActivity
import com.example.eventmatchmaker.ui.adapter.AdapterEvent
import com.example.eventmatchmaker.ui.adapter.LoadingStateAdapter

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySearchBinding
    private var name = "a"
    private var category = ""
    private var ageLimit = ""
    private var priceStart = ""
    private var priceEnd = ""
    private var startTime = ""
    private var startTimeCap = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        category = intent.getStringExtra("SELECTED_ITEM") ?: ""
        ageLimit = intent.getStringExtra("AGE_LIMIT_DATA") ?: ""
        startTime = intent.getStringExtra("START_TIME_DATA") ?: ""
        startTimeCap = intent.getStringExtra("START_TIME_CAP_DATA") ?: ""
        priceStart = intent.getStringExtra("MIN_PRICE_DATA") ?: ""
        priceEnd = intent.getStringExtra("MAX_PRICE_DATA") ?: ""

        Log.d("Testinggg", category + ageLimit + startTime + startTimeCap +
                priceStart + priceEnd)

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
            startTime,
            startTimeCap
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