package com.alexsanderfranco.gistapp.database

import androidx.room.TypeConverter
import com.alexsanderfranco.gistapp.database.entity.GistFile
import com.alexsanderfranco.gistapp.database.entity.Owner
import com.squareup.moshi.Moshi
import org.koin.java.KoinJavaComponent.inject

/**
 * As gist files and owners are not individually queried for, it is possible to use a type
 * converter. If they needed to be individually queried, it would be necessary to switch to
 * a solution using foreign keys and relation tables.
 */
class Converters {
    private val moshi: Moshi by inject(Moshi::class.java)

    @TypeConverter
    fun fromListOfGistFiles(value: List<GistFile>): String {
        val adapter = moshi.adapter(Array<GistFile>::class.java)
        return adapter.toJson(value.toTypedArray())
    }

    @TypeConverter
    fun toListOfGistFiles(value: String): List<GistFile> {
        val adapter = moshi.adapter(Array<GistFile>::class.java)
        return adapter.fromJson(value)!!.toList()
    }

    @TypeConverter
    fun fromListOfOwners(value: Owner): String {
        val adapter = moshi.adapter(Owner::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toListOfOwners(value: String): Owner {
        val adapter = moshi.adapter(Owner::class.java)
        return adapter.fromJson(value)!!
    }

}
