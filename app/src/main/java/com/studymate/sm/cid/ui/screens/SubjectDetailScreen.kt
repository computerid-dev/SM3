package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.data.SubjectMaterial
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    viewModel: AppViewModel,
    subjectId: Long,
    onBack: () -> Unit,
    onEditClick: () -> Unit
) {
    val subject by viewModel.subjectById(subjectId).collectAsState(initial = null)
    val materials by viewModel.materialsForSubject(subjectId).collectAsState(initial = emptyList())
    val schedule by viewModel.scheduleForSubject(subjectId).collectAsState(initial = emptyList())

    var showMaterialDialog by remember { mutableStateOf(false) }
    var chapterName by remember { mutableStateOf("") }
    var pageRange by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subject?.name ?: "Detail Pelajaran") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                chapterName = ""; pageRange = ""; summary = ""
                showMaterialDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah materi")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
        ) {
            item {
                subject?.let { s ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Guru: ${s.teacher.ifBlank { "-" }}")
                            if (s.notes.isNotBlank()) {
                                Spacer(Modifier.height(6.dp))
                                Text("Catatan: ${s.notes}", color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Jadwal", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
            }
            if (schedule.isEmpty()) {
                item {
                    Text(
                        "Belum ada jadwal untuk pelajaran ini.",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(schedule) { item ->
                    Text("${item.day}, ${item.startTime} - ${item.endTime}")
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
                Text("Materi / Bab", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
            }
            if (materials.isEmpty()) {
                item {
                    Text(
                        "Belum ada materi dicatat. Tambah lewat tombol + di kanan bawah.",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(materials, key = { it.id }) { material ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(material.chapterName, fontWeight = FontWeight.Medium)
                                if (material.pageRange.isNotBlank()) {
                                    Text("Halaman: ${material.pageRange}", style = MaterialTheme.typography.bodyMedium)
                                }
                                if (material.summary.isNotBlank()) {
                                    Text(material.summary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                }
                            }
                            IconButton(onClick = { viewModel.deleteMaterial(material) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showMaterialDialog) {
        AlertDialog(
            onDismissRequest = { showMaterialDialog = false },
            title = { Text("Tambah Materi") },
            text = {
                Column {
                    OutlinedTextField(
                        value = chapterName,
                        onValueChange = { chapterName = it },
                        label = { Text("Nama bab / topik") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = pageRange,
                        onValueChange = { pageRange = it },
                        label = { Text("Halaman buku (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Ringkasan (opsional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (chapterName.isNotBlank()) {
                        viewModel.saveMaterial(
                            SubjectMaterial(
                                subjectId = subjectId,
                                chapterName = chapterName.trim(),
                                pageRange = pageRange.trim(),
                                summary = summary.trim()
                            )
                        )
                        showMaterialDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showMaterialDialog = false }) { Text("Batal") }
            }
        )
    }
}
