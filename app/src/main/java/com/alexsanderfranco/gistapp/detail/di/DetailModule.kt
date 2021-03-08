package com.alexsanderfranco.gistapp.detail.di

import com.alexsanderfranco.gistapp.detail.repository.DetailRepository
import com.alexsanderfranco.gistapp.detail.repository.DetailRepositoryImpl
import com.alexsanderfranco.gistapp.detail.viewmodel.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    single<DetailRepository> { DetailRepositoryImpl(gistDao = get()) }
    viewModel { DetailViewModel(repository = get()) }
}