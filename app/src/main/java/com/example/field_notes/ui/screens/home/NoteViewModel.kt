package com.example.field_notes.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.field_notes.data.repository.NoteRepository
import com.example.field_notes.domain.model.Note
import com.example.field_notes.domain.model.NoteCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel (private val repository: NoteRepository) : ViewModel() {

    private val _allNotes = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<NoteCategory?>(null)

    val notes: StateFlow<List<Note>> = combine(
        _allNotes,
        searchQuery,
        selectedCategory
    ) { allNotes, query, category ->
        allNotes
            .filter { note->
                query.isBlank() || note.title.contains(query, ignoreCase = true)
            }
            .filter{ note ->
                category == null || note.category == category
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }


}