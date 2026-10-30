package com.example.roomdemov2

import android.app.Application
import androidx.room.Room
import com.example.roomdemov2.network.KtorClient
import com.example.roomdemov2.room.AppDatabase

class FunFactApp : Application() {

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "FunFactDatabase"
        ).build()
    }

    val ktorClient: KtorClient get() = KtorClient

    val repository: FunFactRepository by lazy {
        FunFactRepository(db.funFactDao(), ktorClient)
    }
}
