package com.example.a2courseviewer

import kotlinx.coroutines.flow.Flow

/**
 * Middle layer between the ViewModel and the Room database.
 * Only one instance exists in the app (created in CourseApplication).
 */
class Courserepo(private val courseDao: CourseDao) {

    val courses: Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun addCourse(course: Course) {
        courseDao.insert(course)
    }

    suspend fun deleteCourse(course: Course) {
        courseDao.delete(course)
    }
}