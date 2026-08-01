package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.studymate.sm.cid.data.Exam
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.ui.components.ItemCard
import com.studymate.sm.cid.ui.components.showDatePicker
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val exams by viewModel.exams.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var materialCovered by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedSubjectId by remember { mutableStateOf<Long?>(null) }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    FeatureScaffold(
        title = "Ujian / Ulangan",
        onMenuClick = onMenuClick,
        onAddClick = {
            name = ""; materialCovered = ""; date = System.currentTimeMillis(); selectedSubjectId = null
            showDialog = true
        }
    ) { padding ->
        if (exams.isEmpty()) {
            EmptyState("Belum ada jadwal ujian. Semoga tetap lancar belajarnya!", padding)
        } else {
            val sorted = exams.sortedBy { it.dateMillis }
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(sorted, key = { it.id }) { exam ->
                    val subjectName = subjects.find { it.id == exam.subjectId }?.name
                    ItemCard(
                        title = exam.name,
                        subtitle = listOfNotNull(subjectName, Formatters.date(exam.dateMillis)).joinToString(" • "),
                        onDelete = { viewModel.deleteExam(exam) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tambah Jadwal Ujian") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama ujian") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))

                    ExposedDropdownMenuBox(expanded = subjectMenuExpanded, onExpandedChange = { subjectMenuExpanded = it }) {
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

                    OutlinedButton(onClick = { showDatePicker(context, date) { date = it } }, modifier = Modifier.fillMaxWidth()) {
                        Text("Tanggal: ${Formatters.date(date)}")
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))

                    OutlinedTextField(
                        value = materialCovered,
                        onValueChange = { materialCovered = it },
                        label = { Text("Materi yang diujikan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isNotBlank()) {
                        viewModel.saveExam(
                            Exam(
                                name = name.trim(),
                                subjectId = selectedSubjectId,
                                dateMillis = date,
                                materialCovered = materialCovered.trim()
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
