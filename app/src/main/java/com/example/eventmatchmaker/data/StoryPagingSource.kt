package com.example.eventmatchmaker.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.eventmatchmaker.data.response.DataItem
import com.example.eventmatchmaker.data.retrofit.ApiServiceFactory

class StoryPagingSource(private val token: String,
                        private val name: String, private val category: String,
                        private val ageLimit: String, private val priceStart: String,
                        private val priceEnd: String, private val startTime: String,
                        private val startTimeCap: String
) : PagingSource<Int, DataItem>() {


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
            val response = apiService.getEventSearch(mapOf("name" to name,
                "categories" to category, "age_limit" to ageLimit,
                "price_start" to priceStart, "price_end" to priceEnd,
                "start_time" to startTime, "start_time_cap" to startTimeCap))
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