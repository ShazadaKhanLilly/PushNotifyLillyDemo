package com.example.pushnotifylillydemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pushnotifylillydemo.data.User
import com.example.pushnotifylillydemo.data.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class PushNotifyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}