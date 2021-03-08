package com.alexsanderfranco.gistapp.detail.repository

import com.alexsanderfranco.gistapp.database.entity.Gist

interface DetailRepository {
    fun toggleFavorite(gist: Gist)
}