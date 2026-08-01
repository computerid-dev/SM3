package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.studymate.sm.cid.data.NoteItem
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val notes by viewModel.notes.collectAsState()
    val subjects by viewModel.subjects.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedSubjectId by remember { mutableStateOf<Long?>(null) }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    FeatureScaffold(
        title = "Catatan Materi",
        onMenuClick = onMenuClick,
        onAddClick = { title = ""; content = ""; selectedSubjectId = null; showDialog = true }
    ) { padding ->
        if (notes.isEmpty()) {
            EmptyState("Belum ada catatan. Yuk mulai rangkum materi belajarmu!", padding)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(notes, key = { it.id }) { note ->
                    val subjectName = subjects.find { it.id == note.subjectId }?.name
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(note.title, fontWeight = FontWeight.Medium)
                                if (subjectName != null) {
                                    Text(subjectName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                                }
                                Text(
                                    note.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline,
                                    maxLines = 3
                                )
                            }
                            IconButton(onClick = { viewModel.deleteNote(note) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tambah Catatan") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Judul catatan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    ExposedDropdownMenuBox(expanded = subjectMenuExpanded, onExpandedChange = { subjectMenuExpanded = it }) {
                        OutlinedTextField(
                            value = subjects.find { it.id == selectedSubjectId }?.name ?: "Tanpa pelajaran",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        DropdownMenu(expanded = subjectMenuExpanded, onDismissRequest = { subjectMenuExpanded = false }) {
                            DropdownMenuItem(text = { Text("Tanpa pelajaran") }, onClick = { selectedSubjectId = null; subjectMenuExpanded = false })
                            subjects.forEach { s ->
                                DropdownMenuItem(text = { Text(s.name) }, onClick = { selectedSubjectId = s.id; subjectMenuExpanded = false })
                            }
                        }
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Isi catatan") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        viewModel.saveNote(
                            NoteItem(title = title.trim(), content = content.trim(), subjectId = selectedSubjectId)
                        )
                        showDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal") } }
        )
    }
}
