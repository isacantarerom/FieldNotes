package com.example.field_notes.domain.model

import androidx.compose.ui.graphics.Color

data class Note(
    val id: Int = 0,
    val remoteId: String? = null,
    val title: String,
    val body: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val color: Long = 0xFFFFFFFF,
    val category: NoteCategory = NoteCategory.NOTES,
    val dueDate: Long? = null
)