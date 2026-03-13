package com.example.field_notes.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val body: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)