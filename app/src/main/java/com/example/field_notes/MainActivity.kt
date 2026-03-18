package com.example.field_notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.field_notes.data.local.NoteDatabase
import com.example.field_notes.data.repository.NoteRepository
import com.example.field_notes.ui.screens.home.AddNoteScreen
import com.example.field_notes.ui.screens.home.HomeScreen
import com.example.field_notes.ui.screens.home.NoteViewModel
import com.example.field_notes.ui.theme.FieldnotesTheme
import com.example.field_notes.domain.model.Note
import com.example.field_notes.ui.screens.home.EditNoteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Wire up the database → repository → viewmodel
        val database = NoteDatabase.getDatabase(this)
        val repository = NoteRepository(database.noteDao())
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(repository) as T
            }
        }

        setContent {
            FieldnotesTheme {
                AppNavigation(viewModelFactory)
            }
        }
    }
}

@Composable
fun AppNavigation(factory: ViewModelProvider.Factory) {
    val viewModel: NoteViewModel = viewModel(factory = factory)
    var showAddScreen by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    when {
        showAddScreen -> {
            AddNoteScreen(
                viewModel = viewModel,
                onNavigateBack = { showAddScreen = false }
            )
        }
        noteToEdit != null -> {
            EditNoteScreen(
                note = noteToEdit!!,
                viewModel = viewModel,
                onNavigateBack = { noteToEdit = null }
            )
        }
        else -> {
            HomeScreen(
                viewModel = viewModel,
                onAddNote = { showAddScreen = true },
                onEditNote = { note -> noteToEdit = note}
            )
        }
    }




}