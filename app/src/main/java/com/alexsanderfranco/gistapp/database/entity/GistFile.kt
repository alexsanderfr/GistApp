package com.alexsanderfranco.gistapp.database.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GistFile(
        var filename: String?,
        var type: String?,
) : Parcelable