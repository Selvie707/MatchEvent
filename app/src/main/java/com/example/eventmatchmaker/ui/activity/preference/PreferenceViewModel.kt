package com.example.eventmatchmaker.ui.activity.preference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.eventmatchmaker.data.pref.Result
import com.example.eventmatchmaker.data.repository.UserRepository
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.data.response.DataItemCategories
import kotlinx.coroutines.launch

class PreferenceViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyList = MutableLiveData<List<DataItemCategories>>()
    val storyList: LiveData<List<DataItemCategories>> = _storyList

    companion object {
        private const val TAG = "MainViewModel"
    }

    val preferences: LiveData<PagingData<DataItemCategories>> =
        repository.getCategories().cachedIn(viewModelScope)
}