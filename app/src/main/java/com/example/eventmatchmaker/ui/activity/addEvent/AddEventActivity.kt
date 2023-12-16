package com.example.eventmatchmaker.ui.activity.addEvent

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.data.pref.Result
import com.example.eventmatchmaker.databinding.ActivityAddEventBinding
import com.example.eventmatchmaker.ui.activity.CameraActivity
import com.example.eventmatchmaker.ui.activity.CameraActivity.Companion.CAMERAX_RESULT
import com.example.eventmatchmaker.ui.activity.ViewModelFactory
import com.example.eventmatchmaker.ui.activity.main.MainActivity
import com.example.eventmatchmaker.ui.reduceFileImage
import com.example.eventmatchmaker.ui.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddEventActivity : AppCompatActivity() {

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

        if (!allPermissionsGranted(Manifest.permission.CAMERA)) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnAddEvent.setOnClickListener {
            uploadImage()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnCamera.setOnClickListener {
            startCameraX()
        }
        binding.cbAddLocation.setOnClickListener {
            if (!allPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) && !allPermissionsGranted(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
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
            val description = binding.etEventAbout.text.toString()

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                uploadStory(imageFile, description)
            } else {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val isChecked = binding.cbAddLocation.isChecked
                    val lat = if (isChecked) location.latitude else null
                    val lon = if (isChecked) location.longitude else null

                    uploadStory(imageFile, description, lat, lon)
                }
            }
            showLoading(true)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun uploadStory(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) {
        viewModel.uploadStory(imageFile, description, lat, lon).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, MainActivity::class.java)
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