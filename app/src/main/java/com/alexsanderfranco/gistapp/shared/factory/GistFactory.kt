package com.alexsanderfranco.gistapp.shared.factory

import com.alexsanderfranco.gistapp.database.entity.Gist
import com.github.javafaker.Faker

class GistFactory {
    fun makeGist(): Gist {
        val faker = Faker()
        val owner = OwnerFactory().makeOwner()
        val listOfGistFiles = GistFileFactory().makeListOfGistFiles()
        return Gist(
            id = faker.internet().uuid(),
            files = listOfGistFiles,
            createdAt = faker.date().birthday().toString(),
            description = faker.lorem().word(),
            owner = owner,
            isFavorite = faker.bool().bool()
        )
    }

    fun makeListOfGist(): List<Gist> {
        val size = (10..50).random()
        return generateSequence { makeGist() }.take(size).toList()
    }
}