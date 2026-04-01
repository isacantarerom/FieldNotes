//DAO = Data Access Object
package com.example.field_notes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao{
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getNoteByRemoteId(remoteId: String) : NoteEntity?

    @Insert
    suspend fun insertNoteAndGetId(note: NoteEntity): Long

    @Query("UPDATE notes SET remoteId = :remoteId WHERE id = :localId")
    suspend fun updateRemoteId(localId: Int, remoteId: String)

}


/*
* What's going on here?
@Dao → tells Room "this is where the database questions live"
@Query → raw SQL. "give me all notes, newest first"
@Insert, @Update, @Delete → Room handles the SQL for these automatically, you don't have to write it
suspend → Kotlin's way of saying "this runs in the background, not on the main thread". Database operations can be slow so they should never block the UI. You'll hear the word coroutines for this — we'll get to that.
Flow → this one is cool. Instead of asking the database once and getting a list back, Flow means "keep watching the database and automatically tell me whenever something changes". So your UI updates in real time without you doing anything extra. 🔥
*
* */