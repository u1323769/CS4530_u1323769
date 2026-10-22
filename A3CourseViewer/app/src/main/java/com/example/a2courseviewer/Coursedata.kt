package com.example.a2courseviewer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class Coursedata : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: Coursedata? = null

        fun getDatabase(context: Context): Coursedata {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    Coursedata::class.java,
                    "course_data"
                ).build().also { INSTANCE = it }
            }
        }
    }
}