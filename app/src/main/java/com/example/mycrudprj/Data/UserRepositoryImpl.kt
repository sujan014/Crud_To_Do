package com.example.mycrudprj.Data

import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val dao: UserDao
): UserRepository {
    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    override fun getAllNames(): Flow<List<User>> {
        return dao.getAllNames()
    }

    override suspend fun getUserById(id: Int): User?{
        return dao.getUserById(id = id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}