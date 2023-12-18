package com.example.eventmatchmaker.ui.activity.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmatchmaker.data.pref.Result
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.data.response.DataItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyList = MutableLiveData<List<DataItem>>()
    val storyList: LiveData<List<DataItem>> = _storyList

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getStoriesLocation() {
        viewModelScope.launch {
            when (val result = repository.getStoriesLocation()) {
                is Result.Success -> {
                    _storyList.value = result.data!!
                }
                is Result.Failed -> {
                    Log.e(TAG, "onFailure")
                }
                else -> {
                }
            }
        }
    }
}