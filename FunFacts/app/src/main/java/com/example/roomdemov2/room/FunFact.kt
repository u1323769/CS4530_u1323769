package com.example.roomdemov2.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
@Entity(tableName = "fun_facts", indices = [Index(value = ["text"], unique = true)])
data class FunFact(
    @Transient
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val text: String,

    val source: String = "",

    @SerialName("source_url")
    val sourceUrl: String = "",

    val language: String = "",

    val permalink: String = ""
)
