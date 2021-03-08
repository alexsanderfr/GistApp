package com.alexsanderfranco.gistapp.list.repository

import com.alexsanderfranco.gistapp.api.GitHubApi
import com.alexsanderfranco.gistapp.api.utils.ApiUtils
import com.alexsanderfranco.gistapp.database.dao.GistDao
import com.alexsanderfranco.gistapp.database.entity.Gist

class GistListRepositoryImpl(
    private val api: GitHubApi,
    private val gistDao: GistDao
) : GistListRepository {

    override suspend fun fetchGistList(page: Int): List<Gist> {
        val response = api.getPublicGists(page)
        if (response.isSuccessful) {
            response.body()?.let {
                val gists = ApiUtils.mapResponseListToObjectList(it)
                gists.forEach { item -> item.isFavorite = gistDao.getAll().contains(item) }
                return gists
            }
        }
        return listOf()
    }

    override fun fetchFavorites(): List<Gist> {
        return gistDao.getAll()
    }

    override fun toggleFavorite(gist: Gist) {
        if (gist.isFavorite) {
            gistDao.delete(gist)
        } else {
            gistDao.insert(gist)
        }
    }
}