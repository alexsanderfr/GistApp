package com.alexsanderfranco.gistapp.api

import com.alexsanderfranco.gistapp.api.response.GistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    @GET("gists/public")
    suspend fun getPublicGists(@Query("page") page: Int?): Response<List<GistResponse>>
}