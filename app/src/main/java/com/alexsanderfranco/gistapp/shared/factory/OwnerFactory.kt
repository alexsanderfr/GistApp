package com.alexsanderfranco.gistapp.shared.factory

import com.alexsanderfranco.gistapp.database.entity.Owner
import com.github.javafaker.Faker

class OwnerFactory {
    fun makeOwner(): Owner {
        val faker = Faker()
        return Owner(login = faker.name().username(), avatarUrl = faker.internet().image())
    }
}