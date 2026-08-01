package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.data.ScheduleItem
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.ItemCard
import com.studymate.sm.cid.ui.components.showTimePicker
import com.studymate.sm.cid.viewmodel.AppViewModel

private val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val schedule by viewModel.schedule.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val context = LocalContext.current

    var selectedDay by remember { mutableStateOf(hariList[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("07:00") }
    var endTime by remember { mutableStateOf("08:30") }
    var selectedSubjectId by remember { mutableStateOf<Long?>(null) }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    FeatureScaffold(
        title = "Jadwal Pelajaran",
        onMenuClick = onMenuClick,
        onAddClick = {
            startTime = "07:00"; endTime = "08:30"; selectedSubjectId = subjects.firstOrNull()?.id
            showDialog = true
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyRow(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                items(hariList) { hari ->
                    FilterChip(
                        selected = hari == selectedDay,
                        onClick = { selectedDay = hari },
                        label = { Text(hari) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            val itemsForDay = schedule.filter { it.day == selectedDay }.sortedBy { it.startTime }
            if (itemsForDay.isEmpty()) {
                EmptyState("Belum ada jadwal di hari $selectedDay.", Modifier.fillMaxSize())
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(itemsForDay, key = { it.id }) { item ->
                        val subjectName = subjects.find { it.id == item.subjectId }?.name ?: "Pelajaran"
                        ItemCard(
                            title = subjectName,
                            subtitle = "${item.startTime} - ${item.endTime}",
                            onDelete = { viewModel.deleteScheduleItem(item) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tambah Jadwal - $selectedDay") },
            text = {
                Column {
                    ExposedDropdownMenuBox(expanded = subjectMenuExpanded, onExpandedChange = { subjectMenuExpanded = it }) {
                        OutlinedTextField(
                            value = subjects.find { it.id == selectedSubjectId }?.name ?: "Pilih pelajaran",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectMenuExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        DropdownMenu(expanded = subjectMenuExpanded, onDismissRequest = { subjectMenuExpanded = false }) {
                            subjects.forEach { s ->
                                DropdownMenuItem(text = { Text(s.name) }, onClick = { selectedSubjectId = s.id; subjectMenuExpanded = false })
                            }
                        }
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedButton(onClick = { showTimePicker(context) { startTime = it } }, modifier = Modifier.fillMaxWidth()) {
                        Text("Mulai: $startTime")
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedButton(onClick = { showTimePicker(context) { endTime = it } }, modifier = Modifier.fillMaxWidth()) {
                        Text("Selesai: $endTime")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (selectedSubjectId != null) {
                        viewModel.saveScheduleItem(
                            ScheduleItem(
                                subjectId = selectedSubjectId!!,
                                day = selectedDay,
                                startTime = startTime,
                                endTime = endTime
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
