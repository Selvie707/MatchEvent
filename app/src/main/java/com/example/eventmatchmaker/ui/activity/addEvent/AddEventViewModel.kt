package com.example.eventmatchmaker.ui.activity.addEvent

import androidx.lifecycle.ViewModel
import com.example.eventmatchmaker.data.repository.UserRepository
import java.io.File

class AddEventViewModel(private val repository: UserRepository): ViewModel() {
//    fun uploadStory(image: File, ageLimit: Int? = null, capacity: Int, categories: String? = null,
//                    description: String, dressCode: String? = null, endTime: String, name: String, organizer: String? = null, price: Int? = null,
//                    startTime: String, lat: Double? = null, lon: Double? = null) = repository.upload(image, ageLimit, capacity, categories, description,
//        dressCode, endTime, name, organizer, price, startTime, lat, lon)

    fun uploadStory(image: File) = repository.upload(image)
}