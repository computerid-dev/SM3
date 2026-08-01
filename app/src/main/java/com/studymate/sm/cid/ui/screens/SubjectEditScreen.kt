package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.data.Subject
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectEditScreen(
    viewModel: AppViewModel,
    subjectId: Long?, // null = mode tambah baru (Fitur 2), terisi = mode edit (Fitur 15)
    onBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    var name by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    val existingSubject = subjectId?.let { id ->
        viewModel.subjectById(id).collectAsState(initial = null).value
    }

    LaunchedEffect(existingSubject) {
        existingSubject?.let {
            name = it.name
            teacher = it.teacher
            notes = it.notes
            selectedCategoryId = it.categoryId
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (subjectId == null) "Tambah Pelajaran" else "Edit Pelajaran") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama pelajaran") },
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))
            OutlinedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("Nama guru (opsional)") },
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))

            ExposedDropdownMenuBox(
                expanded = categoryMenuExpanded,
                onExpandedChange = { categoryMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = categories.find { it.id == selectedCategoryId }?.name ?: "Tanpa kategori",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategori") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                DropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = { categoryMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Tanpa kategori") },
                        onClick = { selectedCategoryId = null; categoryMenuExpanded = false }
                    )
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = { selectedCategoryId = category.id; categoryMenuExpanded = false }
                        )
                    }
                }
            }
            androidx.compose.foundation.layout.Spacer(Modifier.padding(6.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan tambahan (opsional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(10.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val subject = Subject(
                            id = subjectId ?: 0,
                            name = name.trim(),
                            teacher = teacher.trim(),
                            categoryId = selectedCategoryId,
                            notes = notes.trim()
                        )
                        if (subjectId == null) {
                            viewModel.saveSubject(subject) { onBack() }
                        } else {
                            viewModel.updateSubject(subject)
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (subjectId == null) "Simpan Pelajaran" else "Simpan Perubahan")
            }
        }
    }
}
