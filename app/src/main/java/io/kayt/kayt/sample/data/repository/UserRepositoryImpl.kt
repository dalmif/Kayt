package io.kayt.kayt.sample.data.repository

import io.kayt.kayt.sample.data.fakeUsers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import io.kayt.kayt.sample.domain.entity.User
import io.kayt.kayt.sample.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {

    val usersFlow = MutableStateFlow(fakeUsers)

    override fun getUsers() = usersFlow

    override fun removeUser(id: Int) {
        usersFlow.update {
            it.toMutableList().also { it.removeIf { item -> item.id == id }}
        }
    }

    override fun addUser(user: User) {
        usersFlow.update {
            it.toMutableList().also { it.add(user)}
        }
    }
}