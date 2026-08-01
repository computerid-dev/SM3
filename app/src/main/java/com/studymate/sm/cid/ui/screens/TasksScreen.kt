package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.data.TaskItem
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.ui.components.showDatePicker
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val tasks by viewModel.tasks.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedSubjectId by remember { mutableStateOf<Long?>(null) }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    FeatureScaffold(
        title = "PR / Tugas",
        onMenuClick = onMenuClick,
        onAddClick = {
            name = ""; notes = ""; deadline = System.currentTimeMillis(); selectedSubjectId = null
            showDialog = true
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            EmptyState("Belum ada tugas. Santai dulu atau tambahin PR baru!", padding)
        } else {
            val sorted = tasks.sortedWith(compareBy({ it.isDone }, { it.deadlineMillis }))
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(sorted, key = { it.id }) { task ->
                    val subjectName = subjects.find { it.id == task.subjectId }?.name
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = task.isDone, onCheckedChange = { viewModel.toggleTaskDone(task) })
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    task.name,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                                )
                                Text(
                                    listOfNotNull(subjectName, "Deadline: ${Formatters.date(task.deadlineMillis)}")
                                        .joinToString(" • "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            IconButton(onClick = { viewModel.deleteTask(task) }) {
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
            title = { Text("Tambah Tugas") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama tugas") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))

                    ExposedDropdownMenuBox(
                        expanded = subjectMenuExpanded,
                        onExpandedChange = { subjectMenuExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = subjects.find { it.id == selectedSubjectId }?.name ?: "Pilih pelajaran (opsional)",
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

                    OutlinedButton(
                        onClick = { showDatePicker(context, deadline) { deadline = it } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Deadline: ${Formatters.date(deadline)}")
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Catatan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isNotBlank()) {
                        viewModel.saveTask(
                            TaskItem(
                                name = name.trim(),
                                subjectId = selectedSubjectId,
                                deadlineMillis = deadline,
                                notes = notes.trim()
                            )
                        )
                        showDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal") } }
        )
    }
}
