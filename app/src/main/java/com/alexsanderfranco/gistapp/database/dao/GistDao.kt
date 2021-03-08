package com.alexsanderfranco.gistapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alexsanderfranco.gistapp.database.entity.Gist

@Dao
interface GistDao {
    @Query("SELECT * FROM gist")
    fun getAll(): List<Gist>

    @Query("SELECT * FROM gist WHERE id == :id")
    fun getById(id: String): Gist

    @Insert
    fun insert(gist: Gist)

    @Delete
    fun delete(gist: Gist)
}
    