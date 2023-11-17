package com.example.mycrudprj.Data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Responsible for providing us with methods and communicates with database
// @Query enables us to write raw SQL statement to access database
@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllNames(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}