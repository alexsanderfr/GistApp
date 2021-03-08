package com.alexsanderfranco.gistapp.api.utils

import com.alexsanderfranco.gistapp.api.response.GistResponse
import com.alexsanderfranco.gistapp.database.entity.Gist
import com.alexsanderfranco.gistapp.database.entity.GistFile
import com.alexsanderfranco.gistapp.database.entity.Owner

class ApiUtils {
    companion object {
        /** Useful to avoid carrying fields that aren't necessary for core functionality */
        fun mapResponseListToObjectList(gistResponseList: List<GistResponse>) =
            gistResponseList.map { item ->
                Gist(
                    id = item.id,
                    files = item.files.map { file -> GistFile(file.filename, file.type) },
                    createdAt = item.createdAt,
                    description = item.description,
                    owner = Owner(item.owner.login, item.owner.avatarUrl)
                )
            }
    }
}