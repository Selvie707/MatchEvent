package com.example.eventmatchmaker.data.response

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("data")
	val data: List<DataItem> = emptyList()
)

data class DataItem(

	@field:SerializedName("dress_code")
	val dressCode: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("age_limit")
	val ageLimit: Int? = null,

	@field:SerializedName("end_time")
	val endTime: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("total_likes")
	val totalLikes: Int? = null,

	@field:SerializedName("capacity")
	val capacity: Int? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("organizer")
	val organizer: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("categories")
	val categories: List<String?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("participants")
	val participants: List<String?>? = null
)
