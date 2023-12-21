package com.example.eventmatchmaker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.data.retrofit.ApiServiceFactory

class EventPagingSource(private val token: String) : PagingSource<Int, DataItem>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, DataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val apiService = ApiServiceFactory.getApiService(token)
            val response = apiService.getEvents()
            val responseData = response.data
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}