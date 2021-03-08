package com.alexsanderfranco.gistapp.di.modules

import androidx.room.Room
import com.alexsanderfranco.gistapp.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    single {
        get<AppDatabase>().gistDao()
    }
}