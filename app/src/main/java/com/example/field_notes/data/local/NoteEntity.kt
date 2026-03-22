package com.example.field_notes.data.local

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.field_notes.domain.model.Note
import com.example.field_notes.domain.model.NoteCategory

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val color : Long = 0xFFFFFFFF,
    val category: String = NoteCategory.NOTES.name
)