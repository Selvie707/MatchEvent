package com.example.eventmatchmaker.ui.activity.addEvent

import androidx.lifecycle.ViewModel
import com.example.eventmatchmaker.data.repository.UserRepository
import java.io.File

class AddEventViewModel(private val repository: UserRepository): ViewModel() {
    fun uploadStory(image: File, description: String, lat: Double? = null, lon: Double? = null) = repository.upload(image, description, lat, lon)
}