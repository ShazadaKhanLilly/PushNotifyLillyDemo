package com.example.pushnotifylillydemo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pushnotifylillydemo.data.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}