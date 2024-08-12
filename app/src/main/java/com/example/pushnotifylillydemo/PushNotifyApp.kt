package com.example.pushnotifylillydemo

import android.app.Application
    import androidx.room.Room
    import com.example.pushnotifylillydemo.data.database.PushNotifyDatabase

/**
 * Application class for the PushNotifyApp
 */

class PushNotifyApp : Application() {
        lateinit var db: PushNotifyDatabase

        override fun onCreate() {
            super.onCreate()
            db = Room.databaseBuilder(
                applicationContext,
                PushNotifyDatabase::class.java, "demo-database"
            ).build()
        }
    }