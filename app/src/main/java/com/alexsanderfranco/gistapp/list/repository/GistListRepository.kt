package com.alexsanderfranco.gistapp.list.repository

import com.alexsanderfranco.gistapp.database.entity.Gist

interface GistListRepository {
    suspend fun fetchGistList(page: Int) : List<Gist>
    fun fetchFavorites() : List<Gist>
    fun toggleFavorite(gist: Gist)
}