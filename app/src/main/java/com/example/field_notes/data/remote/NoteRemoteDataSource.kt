package com.example.field_notes.data.remote

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class NoteRemoteDataSource {
    private val client = SupabaseClient.client
    private val table = "notes"

    suspend fun getAllNotes(): List<NoteDto> {
        return client.postgrest[table]
            .select()
            .decodeList<NoteDto>()
    }

    suspend fun insertNote(note: NoteDto) {
        client.postgrest[table]
            .insert(note)
    }

    suspend fun updateNote(note: NoteDto) {
        client.postgrest[table]
            .update(note) {
                filter {
                    eq("id", note.id!!)
                }
            }
    }

    suspend fun delete(remoteId: String) {
        client.postgrest[table]
            .delete {
                filter {
                    eq("id", remoteId)
                }
            }
    }

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }
}