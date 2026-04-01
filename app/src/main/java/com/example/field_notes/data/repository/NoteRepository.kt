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

    //Fetch remote notes and sync into local Room db
    suspend fun syncNotes() {
        val userId = remoteDataSource.getCurrentUserId() ?: return
        try {
            val remoteNotes = remoteDataSource.getAllNotes()
            remoteNotes.forEach { dto ->
                val existing = noteDao.getNoteByRemoteId(dto.id!!)
                if(existing == null) {
                    //new note from remote, insert local
                    noteDao.insertNote(dto.toNote().toEntity())
                }
            }
        } catch (e: Exception) {
            //We are offline TBD if show something to the user
        }
    }

    suspend fun insertNote(note: Note) {
        //Save local first
        val localId = noteDao.insertNoteAndGetId(note.toEntity())

        //Sync to Supabase in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = remoteDataSource.getCurrentUserId() ?: return@launch
                val dto = remoteDataSource.insertNote(note.toDto(userId))

                //Save the remote UUID to local note
                noteDao.updateRemoteId(localId.toInt(), dto.id!!)
            } catch (e: Exception) {
                //TBD sync failed
            }
        }
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())

        //Sync to supabase on background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = remoteDataSource.getCurrentUserId() ?: return@launch
                if(note.remoteId != null) remoteDataSource.updateNote(note.toDto(userId))
            } catch (e: Exception) {
                //TBD
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())

        //Sync to supabase in bg
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(note.remoteId != null) {
                    remoteDataSource.delete(note.remoteId)
                }
            } catch (e: Exception) {
                //TBD
            }
        }
    }
}

//Mapper function:
// Small app / few models  →  mappers in the same file, fine
// Large app / many models →  dedicated NoteMappers.kt makes more sense


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



/* THIS file is the middleman between Note and NoteEntity! 🎉

The Repository only speaks Note to the outside world, and only speaks NoteEntity to the database.
Nobody else needs to know about the translation.
 */