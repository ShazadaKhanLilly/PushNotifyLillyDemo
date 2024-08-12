package com.example.pushnotifylillydemo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User data class
 * @param id: Int
 * @param email: String
 * @param password: String
 * @param drug: String
 */

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val drug: String
)