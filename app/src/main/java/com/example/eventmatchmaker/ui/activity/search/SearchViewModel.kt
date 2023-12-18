package com.example.eventmatchmaker.ui.activity.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.data.response.DataItem

class SearchViewModel(private val repository: UserRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }

    val text: LiveData<String> = _text

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var name = ""
    private var location = ""
    private var category = ""
    private var startDate = ""
    private var endDate = ""
    private var maxAge = ""

//    Function to update the search parameters

    fun getUserStories(
        name: String,
        category: String,
        ageLimit: String,
        priceStart: String,
        priceEnd: String,
        startTime: String
    ): LiveData<PagingData<DataItem>> {
        return repository.getUserStories(name, category, ageLimit, priceStart, priceEnd, startTime)
    }

    val story: LiveData<PagingData<DataItem>> =
        repository.getUserStories("", "", "", "", "", "").cachedIn(viewModelScope)
}