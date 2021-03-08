package com.alexsanderfranco.gistapp.detail.repository

import com.alexsanderfranco.gistapp.database.dao.GistDao
import com.alexsanderfranco.gistapp.database.entity.Gist

class DetailRepositoryImpl(private val gistDao: GistDao) : DetailRepository {

    override fun toggleFavorite(gist: Gist) {
        if (gist.isFavorite) {
            gistDao.delete(gist)
        } else {
            gistDao.insert(gist)
        }
    }
}