package com.example.mycrudprj.Data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    fun getAllNames(): Flow<List<User>>

    suspend fun getUserById(id: Int): User?

    suspend fun deleteAll()
}