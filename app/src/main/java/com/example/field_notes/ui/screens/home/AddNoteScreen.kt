package com.example.field_notes.ui.screens.home
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.field_notes.domain.model.Note
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import com.example.field_notes.domain.model.NoteCategory
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.draw.clip
import androidx.room.util.copy
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var selectedColor by remember { mutableLongStateOf(noteColors.first()) }
    var selectedCategory by remember { mutableStateOf(NoteCategory.NOTES) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Note") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Here's to a brilliant title.") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            ColorPicker(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it }
            )
            CategoryPicker(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Type your awesome note here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = 10
            )
            Button(
                onClick = {
                    if(title.isNotBlank()) {
                        viewModel.insertNote(
                            Note(
                                title = title.trim(),
                                body = body.trim(),
                                color = selectedColor,
                                category = selectedCategory
                            )
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Note")
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor : Long,
    onColorSelected: (Long) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        noteColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.toComposeColor())
                    .border(
                        width = if(selectedColor == color) 3.dp else 1.dp,
                        color = if(selectedColor == color) MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun CategoryPicker(
    selectedCategory: NoteCategory,
    onCategorySelected: (NoteCategory) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(NoteCategory.entries) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.toDisplayName()) }
            )
        }
    }
}

fun NoteCategory.toDisplayName(): String = when (this) {
    NoteCategory.NOTES -> "📝 Notes"
    NoteCategory.HOME -> "🏠 Home"
    NoteCategory.GROCERIES -> "🛒 Groceries"
    NoteCategory.WORK -> "💼 Work"
    NoteCategory.TODO -> "✅ To Do"
    NoteCategory.APPOINTMENTS -> "📅 Appointments"
    NoteCategory.VACATION -> "✈️ Vacation"
    NoteCategory.REMINDERS -> "🔔 Reminders"
}