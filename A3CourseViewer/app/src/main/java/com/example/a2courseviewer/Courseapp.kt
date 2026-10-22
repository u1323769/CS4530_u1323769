package com.example.a2courseviewer

import android.app.Application

class Courseapp : Application() {

    val repository: Courserepo by lazy {
        Courserepo(Coursedata.getDatabase(this).courseDao())
    }
}