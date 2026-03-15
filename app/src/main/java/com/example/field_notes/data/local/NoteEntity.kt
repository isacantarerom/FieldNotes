package com.example.field_notes.data.local

import androidx.room.Entity
import androidx.room.PimaryEntity

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)