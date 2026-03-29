package com.example.field_notes.data.remote

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import android.R.attr.id

class NoteRemoteDataSource {
    private val client = SupabaseClient.client
    private val table = "notes"

    suspend fun getAllNotes(): List<NoteDto> {
        return client.postgrest[table]
            .select()
            .decodeList<NoteDto>()
    }

    suspend fun insertNote(note: NoteDto) : NoteDto {
        return client.postgrest[table]
            .insert(note)
            .decodeSingle<NoteDto>()
    }

    suspend fun updateNote(note: NoteDto) {
        client.postgrest[table]
            .update(note) {
                filter {
                    eq("id", note.id!!)
                }
            }
    }

    suspend fun delete(note: NoteDto) {
        client.postgrest[table]
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    fun getCurrentUserId() : String? {
        return client.auth.currentUserOrNull()?.id
    }

}