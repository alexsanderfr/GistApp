package com.alexsanderfranco.gistapp.di.modules

import com.alexsanderfranco.gistapp.api.GitHubApi
import com.alexsanderfranco.gistapp.api.adapters.ListOfFilesAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    single {
        Retrofit.Builder().baseUrl(GitHubApi.BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(GitHubApi::class.java)
    }

    single {
        Moshi.Builder()
            .add(get<ListOfFilesAdapter>())
            .build()
    }

    single { ListOfFilesAdapter() }
}