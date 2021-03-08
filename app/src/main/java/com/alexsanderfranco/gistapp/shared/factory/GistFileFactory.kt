package com.alexsanderfranco.gistapp.shared.factory

import com.alexsanderfranco.gistapp.database.entity.GistFile
import com.github.javafaker.Faker

class GistFileFactory {
    private fun makeGistFile(): GistFile {
        val faker = Faker()
        return GistFile(filename = faker.file().fileName(), type = faker.file().mimeType())
    }

    fun makeListOfGistFiles(): List<GistFile> {
        val size = (1..5).random()
        return generateSequence { makeGistFile() }.take(size).toList()
    }
}