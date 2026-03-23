package com.example.field_notes.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.material3.FilterChip
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.remember
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import com.example.field_notes.domain.model.Note
import com.example.field_notes.domain.model.NoteCategory

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen (
    viewModel: NoteViewModel,
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val context = LocalContext.current

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.searchQuery.value = it }
            )
            CategoryFilterRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.selectedCategory.value = it }
            )
            if(notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📋",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "No notes yet...",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tap + to create your first note",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(note.id) { visible = true }

                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300)),
                        ) {
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if(value == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deleteNote(note)
                                        true
                                    } else false
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp)
                                            .clip(MaterialTheme.shapes.medium)
                                            .background(Color.Red),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                    }
                                }
                            ) {
                                NoteCard(
                                    note = note,
                                    modifier = Modifier.animateItem(),
                                    onToggleComplete = {
                                        viewModel.updateNote(note.copy(isCompleted = !note.isCompleted))
                                    },
                                    onDelete = { viewModel.deleteNote(note) },
                                    onEditNote = { onEditNote(note) },
                                    onShare = { shareNote(context, note) }
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEditNote: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEditNote() },
                onLongClick = { onShare() }
            ),
        colors = CardDefaults.cardColors(
            containerColor = note.color.toComposeColor()
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = note.isCompleted,
                onCheckedChange = {onToggleComplete()},
                colors = CheckboxDefaults.colors(
                    checkmarkColor = note.color.contentColorFor(),
                    uncheckedColor = note.color.contentColorFor(),
                    checkedColor = note.color.contentColorFor().copy(alpha = 0.5f)
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = note.color.contentColorFor(),
                    textDecoration = if (note.isCompleted)
                        TextDecoration.LineThrough else TextDecoration.None
                )
                if(note.body.isNotBlank()) {
                    Text(
                        text = note.body,
                        style = MaterialTheme.typography.titleMedium,
                        color = note.color.contentColorFor(),
                        textDecoration = if(note.isCompleted)
                            TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = note.color.contentColorFor()
                    )
            }
        }
    }
}

@Composable
fun SearchBar(
    query : String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text ("🔍 Search notes...") },
        modifier = Modifier.
        fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun CategoryFilterRow(
    selectedCategory: NoteCategory?,
    onCategorySelected: (NoteCategory?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("🗂️ All") }
            )
        }
        items(NoteCategory.entries) { category ->
            FilterChip(
                selected =  selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.toDisplayName()) }
            )
        }
    }
}

fun shareNote(context: android.content.Context, note: Note) {
    val text = buildString {
        appendLine( "📋 ${note.title}")
        if(note.body.isNotBlank()){
            appendLine()
            appendLine(note.body)
        }
        appendLine()
        appendLine("Category: ${note.category.toDisplayName()}")
        appendLine("Status: ${if (note.isCompleted) "✅ Complete" else "⏳ Pending"}")
    }

    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_SUBJECT, note.title)
        putExtra(android.content.Intent.EXTRA_TEXT, text)
    }
    context.startActivity(
        android.content.Intent.createChooser(intent, "Share note via...")
    )
}
