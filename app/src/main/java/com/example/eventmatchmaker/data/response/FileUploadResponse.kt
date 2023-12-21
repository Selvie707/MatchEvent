package com.example.eventmatchmaker.data.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("id")
	val id: String = "",

	@field:SerializedName("message")
	val message: String = ""
)
