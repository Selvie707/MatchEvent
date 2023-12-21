package com.example.eventmatchmaker.ui.activity.addEvent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.data.pref.Result
import com.example.eventmatchmaker.databinding.ActivityAddEventBinding
import com.example.eventmatchmaker.ui.activity.CameraActivity
import com.example.eventmatchmaker.ui.activity.CameraActivity.Companion.CAMERAX_RESULT
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.profile.ProfileActivity
import com.example.eventmatchmaker.ui.reduceFileImage
import com.example.eventmatchmaker.ui.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import nl.joery.timerangepicker.TimeRangePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class AddEventActivity : AppCompatActivity() {

    private lateinit var btnShowDateRangePicker: EditText
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityAddEventBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val viewModel by viewModels<AddEventViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(resources.getString(R.string.permissionGranted))
            } else {
                showToast(resources.getString(R.string.permissionDenied))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pbProgressBar.visibility = View.GONE
        binding.etEventLocation.visibility = View.GONE
        binding.tvEventLocation.visibility = View.GONE
        binding.etEventPrice.visibility = View.GONE
        binding.tvEventPrice.visibility = View.GONE

        if (!allPermissionsGranted(Manifest.permission.CAMERA)) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnAddEvent.setOnClickListener {
            uploadImage()
        }

        if (binding.clChooseImage.visibility == View.VISIBLE) {
            binding.fabCloseChooseImage.setOnClickListener {
                binding.clChooseImage.visibility = View.GONE
            }
        }

        binding.ivEventPicture.setOnClickListener {

            binding.clChooseImage.visibility = View.VISIBLE

            binding.btnGallery.setOnClickListener {
                startGallery()
                binding.clChooseImage.visibility = View.GONE
            }

            binding.btnCamera.setOnClickListener {
                startCameraX()
                binding.clChooseImage.visibility = View.GONE
            }
        }

        binding.cbAddLocation.setOnClickListener {

            if (binding.cbAddLocation.isChecked) {
                binding.tvEventLocation.visibility = View.VISIBLE
                binding.etEventLocation.visibility = View.VISIBLE
            } else {
                binding.tvEventLocation.visibility = View.GONE
                binding.etEventLocation.visibility = View.GONE
            }

            if (!allPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) && !allPermissionsGranted(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        binding.etEventDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select Date Range")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.supportFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding.etEventDate.hint = convertTimeToDate(it.first) + "/" + convertTimeToDate(it.second)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }

        binding.etEventTime.setOnClickListener {
            binding.clChooseTime.visibility = View.VISIBLE
        }

        binding.trpTimeRange.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                Log.d("TimeRangePicker", "Start time: $startTime")
                binding.tvStartTime.text = startTime.toString()
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                Log.d("TimeRangePicker", "End time: " + endTime.hour)
                binding.tvEndTime.text = endTime.toString()
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                Log.d("TimeRangePicker", "Duration: " + duration.hour)
            }
        })

        binding.fabCloseTime.setOnClickListener {
            binding.clChooseTime.visibility = View.GONE
        }

        binding.fabOkeTime.setOnClickListener {
            binding.etEventTime.hint = binding.tvStartTime.text.toString() + " - " + binding.tvEndTime.text.toString()
            binding.clChooseTime.visibility = View.GONE
        }

        binding.smPrice.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, _ ->
            if (binding.smPrice.isChecked) {
                binding.tvEventPrice.visibility = View.VISIBLE
                binding.etEventPrice.visibility = View.VISIBLE
            } else {
                binding.tvEventPrice.visibility = View.GONE
                binding.etEventPrice.visibility = View.GONE
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }

    private fun allPermissionsGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(resources.getString(R.string.noImageSelected))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivEventPicture.setImageURI(it)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
//            val ageLimit = 18
//            val capacity = 707
//            val categories = "adventure"
//            val description = "abc description"
//            val dressCode = "abc dress code"
//            val endTime = "2023-12-01T20:00:00.000Z"
//            val name = "abc name"
//            val organizer = "abc organizer"
//            val price = 120000
//            val startTime = "2023-11-01T20:00:00.000Z"

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
//                uploadStory(imageFile, ageLimit, capacity, categories, description, dressCode, endTime,
//                    name, organizer, price, startTime)

                uploadStory(imageFile)
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val isChecked = binding.cbAddLocation.isChecked
                    val lat = if (isChecked) location.latitude else null
                    val lon = if (isChecked) location.longitude else null

//                    uploadStory(imageFile, ageLimit, capacity, categories, description, dressCode, endTime,
//                        name, organizer, price, startTime, lat, lon)

                    uploadStory(imageFile)
                }
            }
            showLoading(true)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

//    private fun uploadStory(
//        imageFile: File, ageLimit: Int? = null, capacity: Int, categories: String? = null,
//        description: String, dressCode: String? = null, endTime: String, name: String, organizer: String? = null, price: Int? = null,
//        startTime: String, lat: Double? = null, lon: Double? = null
//    ) {
//        viewModel.uploadStory(imageFile, ageLimit, capacity, categories, description, dressCode,
//            endTime, name, organizer, price, startTime, lat, lon).observe(this) { result ->
//            if (result != null) {
//                when (result) {
//                    is Result.Loading -> {
//                        showLoading(true)
//                    }
//                    is Result.Success -> {
//                        showLoading(false)
//                        val intent = Intent(this, ProfileActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        showRegistrationSuccessDialog(intent)
//                    }
//                    is Result.Failed -> {
//                        showLoading(false)
//                        showToast(resources.getString(R.string.toast_upload_failed))
//                    }
//                }
//            }
//        }
//    }

    private fun uploadStory(imageFile: File) {
        viewModel.uploadStory(imageFile).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        showRegistrationSuccessDialog(intent)
                    }
                    is Result.Failed -> {
                        showLoading(false)
                        showToast(resources.getString(R.string.toast_upload_failed))
                    }
                }
            }
        }
    }

    private fun showRegistrationSuccessDialog(intent: Intent) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this).apply {
                val dialogMessage = "New event has been added!"

                setTitle(resources.getString(R.string.dialogRegisterSuccess))
                setMessage(dialogMessage)
                setPositiveButton(resources.getString(R.string.oke)) { _, _ ->
                    startActivity(intent)
                    finish()
                }
                create().show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}