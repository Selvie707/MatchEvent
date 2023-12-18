package com.example.eventmatchmaker.ui.activity.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.databinding.ActivityMainBinding
import com.example.eventmatchmaker.ui.activity.DetailActivity
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.map.MapsActivity
import com.example.eventmatchmaker.ui.activity.onboarding.OnboardingActivity
import com.example.eventmatchmaker.ui.activity.profile.ProfileActivity
import com.example.eventmatchmaker.ui.activity.search.SearchActivity
import com.example.eventmatchmaker.ui.adapter.AdapterEvent
import com.example.eventmatchmaker.ui.adapter.LoadingStateAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var token: String
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateTo("WelcomeActivity")
            } else {
                user.token.let {
                    token = it
                    binding.fabAddStory.setOnClickListener {
                        navigateTo("AddStoryActivity")
                    }

                    val userWelcome = resources.getString(R.string.userWelcome) + user.name + "!"
                    binding.tvUsername.text = user.name
                    binding.tvHello.text = userWelcome

                    if (!::token.isInitialized) {
                        token = it
                    }
                }
            }
        }

        viewModel.isLoading.observe(this) {loading ->
            showLoading(loading)
        }

        binding.navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        setupUI()
        setupView()
        setupAction()
        viewModelObserve()

        binding.tvUserLocation.setOnClickListener {
            showToast("testing")
            getLocation()
        }

        setReviewData()
    }

    private fun getLocation() {
        showToast("testing again")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }

        //get lat and lon
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                val lat = it.latitude
                val lon = it.longitude

                val address = getAddressFromLocation(lat, lon)

                showToast(address)

                binding.tvUserLocation.text = address
            }
        }
    }

    private fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ): String {
        val geocoder = Geocoder(this, java.util.Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                val stringBuilder = StringBuilder()

                // Construct the address string using available address lines
                for (i in 0..address.maxAddressLineIndex) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n")
                }

                addressText = stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                showToast("Permission denied")
            }
        }
    }

    private fun setupUI() {
        binding.pbProgressBar.visibility = View.GONE
        val layoutManagerRecommendation = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerThisWeekend = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvRecommendation.layoutManager = layoutManagerRecommendation
        binding.rvRecommendation.addItemDecoration(DividerItemDecoration(this, layoutManagerRecommendation.orientation))

        binding.rvThisWeekend.layoutManager = layoutManagerThisWeekend
        binding.rvThisWeekend.addItemDecoration(DividerItemDecoration(this, layoutManagerThisWeekend.orientation))
    }

    private fun viewModelObserve() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateTo("WelcomeActivity")
            } else {
                user.token.let {
                    token = it
                    binding.fabAddStory.setOnClickListener {
                        navigateTo("AddStoryActivity")
                    }

                    val userWelcome = resources.getString(R.string.userWelcome) + user.name + "!"
//                    binding.nameTextView.text = userWelcome

                    if (!::token.isInitialized) {
                        token = it
                    }
                }
            }
        }
        viewModel.isLoading.observe(this) {loading ->
            showLoading(loading)
        }
    }

    private fun navigateTo(activityName: String) {
        when (activityName) {
            "WelcomeActivity" -> {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }
            "AddStoryActivity" -> {
                startActivity(Intent(this, DetailActivity::class.java))
            }
            else -> {
                showToast(resources.getString(R.string.toastNavigateError))
            }
        }
    }

    private fun setupView() {
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

    private fun setupAction() {
        binding.settingButton.setOnClickListener {
            if (binding.logoutButton.isVisible) {
                binding.logoutButton.visibility = View.GONE
                binding.languageButton.visibility = View.GONE
                binding.mapButton.visibility = View.GONE
            } else {
                binding.logoutButton.visibility = View.VISIBLE
                binding.languageButton.visibility = View.VISIBLE
                binding.mapButton.visibility = View.VISIBLE
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        binding.languageButton.setOnClickListener {
            switchLanguage()
        }

        binding.mapButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun switchLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun setReviewData() {
        val adapter = AdapterEvent()

        binding.rvRecommendation.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        binding.rvThisWeekend.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}