package com.example.field_notes.data.repository

import com.example.field_notes.data.local.NoteDao
import com.example.field_notes.data.local.NoteEntity
import com.example.field_notes.data.remote.NoteRemoteDataSource
import com.example.field_notes.data.remote.toNote
import com.example.field_notes.data.remote.toDto
import com.example.field_notes.domain.model.Note
import com.example.field_notes.domain.model.NoteCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NoteRepository(
    private val noteDao: NoteDao,
    private val remoteDataSource: NoteRemoteDataSource = NoteRemoteDataSource()
) {
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { entity -> entity.toNote() }
        }
    }

    suspend fun syncNotes() {
        val userId = remoteDataSource.getCurrentUserId() ?: return
        try {
            val remoteNotes = remoteDataSource.getAllNotes()
            remoteNotes.forEach { dto ->
                val existing = noteDao.getNoteByRemoteId(dto.id!!)
                if (existing == null) {
                    noteDao.insertNote(dto.toNote().toEntity())
                }
            }
        } catch (e: Exception) {
            // Offline — silently skip
        }
    }

    suspend fun insertNote(note: Note) {
        val localId = noteDao.insertNoteAndGetId(note.toEntity())
        android.util.Log.d("FieldNotes", "✅ Saved locally with id: $localId")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = remoteDataSource.getCurrentUserId()
                android.util.Log.d("FieldNotes", "👤 Current user id: $userId")

                if (userId == null) {
                    android.util.Log.d("FieldNotes", "❌ No user logged in, skipping remote insert")
                    return@launch
                }

                remoteDataSource.insertNote(note.toDto(userId))
                android.util.Log.d("FieldNotes", "☁️ Saved to Supabase!")
            } catch (e: Exception) {
                android.util.Log.e("FieldNotes", "💥 Remote insert failed: ${e.message}", e)
            }
        }
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = remoteDataSource.getCurrentUserId() ?: return@launch
                if (note.remoteId != null) remoteDataSource.updateNote(note.toDto(userId))
            } catch (e: Exception) {
                // TBD
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (note.remoteId != null) {
                    remoteDataSource.delete(note.remoteId)
                }
            } catch (e: Exception) {
                // TBD
            }
        }
    }
}

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        remoteId = remoteId,
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color,
        category = NoteCategory.valueOf(category),
        dueDate = dueDate
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        remoteId = remoteId,
        title = title,
        body = body,
        isCompleted = isCompleted,
        createdAt = createdAt,
        color = color,
        category = category.name,
        dueDate = dueDate
    )
}