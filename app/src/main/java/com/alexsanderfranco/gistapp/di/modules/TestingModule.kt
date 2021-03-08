package com.alexsanderfranco.gistapp.di.modules

import com.alexsanderfranco.gistapp.shared.test.SimpleIdlingResource
import org.koin.dsl.module

val testingModule = module {
    single {
        SimpleIdlingResource()
    }
}