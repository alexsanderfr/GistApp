package com.alexsanderfranco.gistapp.list.di

import com.alexsanderfranco.gistapp.list.repository.GistListRepository
import com.alexsanderfranco.gistapp.list.repository.GistListRepositoryImpl
import com.alexsanderfranco.gistapp.list.view.GistFileListAdapter
import com.alexsanderfranco.gistapp.list.view.GistListAdapter
import com.alexsanderfranco.gistapp.list.viewmodel.GistListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gistListModule = module {
    single<GistListRepository> { GistListRepositoryImpl(api = get(), gistDao = get()) }
    factory { GistListAdapter() }
    factory { GistFileListAdapter() }
    viewModel { GistListViewModel(repository = get(), idlingResource = get()) }
}