package com.example.eventmatchmaker.data.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
	@field:SerializedName("joined_event")
	val joinedEvent: List<String?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)