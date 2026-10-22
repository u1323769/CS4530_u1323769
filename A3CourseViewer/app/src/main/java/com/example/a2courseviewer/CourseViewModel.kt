package com.example.a2courseviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: Courserepo
) : ViewModel() {

    val courses: StateFlow<List<Course>> = repository.courses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addCourse(department: String, number: String, location: String) {
        viewModelScope.launch {
            repository.addCourse(
                Course(
                    department = department,
                    number = number,
                    location = location
                )
            )
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }

    companion object {
        // Hands the single app-wide repository to the ViewModel.
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as Courseapp
                CourseViewModel(application.repository)
            }
        }
    }
}