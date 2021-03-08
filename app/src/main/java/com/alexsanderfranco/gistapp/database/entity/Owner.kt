package com.alexsanderfranco.gistapp.database.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
        var login: String?,
        var avatarUrl: String?,
) : Parcelable