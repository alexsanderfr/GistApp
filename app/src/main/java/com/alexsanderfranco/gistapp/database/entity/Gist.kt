package com.alexsanderfranco.gistapp.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Gist(
        @PrimaryKey
        var id: String,
        var files: List<GistFile>?,
        var createdAt: String?,
        var description: String?,
        var owner: Owner?,
        var isFavorite: Boolean = false,
) : Parcelable