package com.example.field_notes.ui.screens.home


import android.icu.number.NumberFormatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.field_notes.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    viewModel: NoteViewModel,
    onAddNote: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Field Notes")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                NoteCard(
                    note = note,
                    onToggleComplete = {
                        viewModel.updateNote(note.copy(isCompleted = !note.isCompleted))
                    },
                    onDelete = {
                        viewModel.deleteNote(note)
                    }
                )
            }
        }
    }
}


@Composable
fun NoteCard(
    note: Note,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = note.isCompleted,
                onCheckedChange = {onToggleComplete()}
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (note.isCompleted)
                        TextDecoration.LineThrough else TextDecoration.None
                )
                if(note.body.isNotBlank()) {
                    Text(
                        text = note.body,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if(note.isCompleted)
                            TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Note")
            }
        }
    }
}