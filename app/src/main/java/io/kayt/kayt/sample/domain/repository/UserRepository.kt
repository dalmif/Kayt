package io.kayt.kayt.sample.domain.repository

import kotlinx.coroutines.flow.Flow
import io.kayt.kayt.sample.domain.entity.User

interface UserRepository {
    fun getUsers () : Flow<List<User>>
    fun removeUser (id : Int)
    fun addUser (user : User)
}