package com.example.field_notes.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.field_notes.domain.model.Note
import com.example.field_notes.domain.model.NoteCategory

@Serializable
data class NoteDto(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String? = null,
    val title: String,
    val body: String = "",
    @SerialName("is_completed")
    val isCompleted: Boolean = false,
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    val color: Long = 0xFFFFFF,
    val category: String = NoteCategory.NOTES.name,
    @SerialName("due_date")
    val dueDate: Long? = null

)

fun NoteDto.toNote() : Note {
    return Note(
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color,
        category = NoteCategory.valueOf(category),
        dueDate = dueDate
    )
}

fun Note.toDto(userId: String): NoteDto {
    return NoteDto (
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color,
        category = category.name,
        dueDate = dueDate
    )
}