package com.example.a2courseviewer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY department, number")
    fun getAllCourses(): Flow<List<Course>>

    @Insert
    suspend fun insert(course: Course)

    @Delete
    suspend fun delete(course: Course)
}