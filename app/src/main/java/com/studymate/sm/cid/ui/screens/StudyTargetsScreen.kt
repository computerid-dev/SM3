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
import com.studymate.sm.cid.data.StudyTarget
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.ui.components.showDatePicker
import com.studymate.sm.cid.viewmodel.AppViewModel

@Composable
fun StudyTargetsScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val targets by viewModel.studyTargets.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf<Long?>(null) }

    FeatureScaffold(
        title = "Target Belajar",
        onMenuClick = onMenuClick,
        onAddClick = { title = ""; description = ""; deadline = null; showDialog = true }
    ) { padding ->
        if (targets.isEmpty()) {
            EmptyState("Belum ada target belajar. Yuk pasang target baru!", padding)
        } else {
            val sorted = targets.sortedBy { it.isDone }
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(sorted, key = { it.id }) { target ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = target.isDone, onCheckedChange = { viewModel.toggleTargetDone(target) })
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    target.title,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = if (target.isDone) TextDecoration.LineThrough else null
                                )
                                if (target.description.isNotBlank()) {
                                    Text(target.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                }
                                if (target.deadlineMillis != null) {
                                    Text(
                                        "Target selesai: ${Formatters.date(target.deadlineMillis)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            IconButton(onClick = { viewModel.deleteStudyTarget(target) }) {
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
            title = { Text("Tambah Target Belajar") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Nama target") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Deskripsi (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedButton(
                        onClick = { showDatePicker(context, deadline) { deadline = it } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (deadline == null) "Pilih target selesai (opsional)" else "Target selesai: ${Formatters.date(deadline!!)}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        viewModel.saveStudyTarget(
                            StudyTarget(title = title.trim(), description = description.trim(), deadlineMillis = deadline)
                        )
                        showDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal") } }
        )
    }
}
