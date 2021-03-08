package com.alexsanderfranco.gistapp.di.utils

import android.app.Application
import com.alexsanderfranco.gistapp.detail.di.detailModule
import com.alexsanderfranco.gistapp.di.modules.apiModule
import com.alexsanderfranco.gistapp.di.modules.databaseModule
import com.alexsanderfranco.gistapp.di.modules.testingModule
import com.alexsanderfranco.gistapp.list.di.gistListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinUtils {
    companion object {
        fun initializeKoin(application: Application) {
            startKoin {
                androidContext(application)
                modules(getModules())
            }
        }

        private fun getModules() =
            listOf(
                apiModule,
                databaseModule,
                testingModule,
                gistListModule,
                detailModule
            )
    }
}