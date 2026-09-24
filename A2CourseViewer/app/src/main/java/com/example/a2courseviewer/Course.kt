package com.example.a2courseviewer

data class Course(
    val department: String,
    val number: String,
    val location: String
) {
    val name: String
        get() = "$department $number"
}