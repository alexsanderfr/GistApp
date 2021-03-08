package com.alexsanderfranco.gistapp.api.response

import com.squareup.moshi.Json

data class GistFileResponse(
	@field:Json(name = "filename") val filename: String,
	@field:Json(name = "type") val type: String,
	@field:Json(name = "language") val language: String,
	@field:Json(name = "raw_url") val rawUrl: String,
	@field:Json(name = "size") val size: Int
)