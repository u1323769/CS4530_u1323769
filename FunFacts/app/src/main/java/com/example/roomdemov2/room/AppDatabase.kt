package com.example.roomdemov2.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FunFact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun funFactDao(): FunFactDao
}
