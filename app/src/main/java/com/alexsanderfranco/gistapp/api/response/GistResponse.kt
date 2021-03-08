package com.alexsanderfranco.gistapp.api.response

import com.squareup.moshi.Json


data class GistResponse(
	@field:Json(name = "url") val url: String,
	@field:Json(name = "forks_url") val forksUrl: String,
	@field:Json(name = "commits_url") val commitsUrl: String,
	@field:Json(name = "id") val id: String,
	@field:Json(name = "node_id") val nodeId: String,
	@field:Json(name = "git_pull_url") val gitPullUrl: String,
	@field:Json(name = "git_push_url") val gitPushUrl: String,
	@field:Json(name = "html_url") val htmlUrl: String,
	@field:Json(name = "files") val files: List<GistFileResponse>,
	@field:Json(name = "public") val public: Boolean,
	@field:Json(name = "created_at") val createdAt: String,
	@field:Json(name = "updated_at") val updatedAt: String,
	@field:Json(name = "description") val description: String,
	@field:Json(name = "comments") val comments: Int,
	@field:Json(name = "user") val user: String,
	@field:Json(name = "comments_url") val commentsUrl: String,
	@field:Json(name = "owner") val owner: OwnerResponse,
	@field:Json(name = "truncated") val truncated: Boolean
)