package com.example.a2courseviewer

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    private val _courses = mutableStateListOf<Course>()

    val courses: List<Course>
        get() = _courses

    fun addCourse(
        department: String,
        number: String,
        location: String
    ) {
        val course = Course(
            department = department,
            number = number,
            location = location
        )

        _courses.add(course)
    }

    fun deleteCourse(course: Course) {
        _courses.remove(course)
    }
}