package com.alexsanderfranco.gistapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexsanderfranco.gistapp.database.dao.GistDao
import com.alexsanderfranco.gistapp.database.entity.Gist


@Database(entities = [Gist::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "gistapp.db"
    }

    abstract fun gistDao(): GistDao
}
