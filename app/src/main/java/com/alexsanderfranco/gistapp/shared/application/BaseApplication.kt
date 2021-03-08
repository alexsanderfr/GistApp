package com.alexsanderfranco.gistapp.shared.application

import android.app.Application
import com.alexsanderfranco.gistapp.di.utils.KoinUtils

/** Necessary to initialize Koin */
@Suppress("unused") // Suppressed because it's used in manifest
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinUtils.initializeKoin(this)
    }
}