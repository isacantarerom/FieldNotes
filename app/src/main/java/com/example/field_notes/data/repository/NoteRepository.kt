package com.example.field_notes.data.repository

import com.example.field_notes.data.local.NoteDao
import com.example.field_notes.data.local.NoteEntity
import com.example.field_notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { entity -> entity.toNote() }
        }
    }

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
}

//Mapper function:
// Small app / few models  →  mappers in the same file, fine
// Large app / many models →  dedicated NoteMappers.kt makes more sense


fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color
    )
}



/* THIS file is the middleman between Note and NoteEntity! 🎉

The Repository only speaks Note to the outside world, and only speaks NoteEntity to the database.
Nobody else needs to know about the translation.
 */