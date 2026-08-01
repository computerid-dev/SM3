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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.studymate.sm.cid.data.CalendarEvent
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.ui.components.ItemCard
import com.studymate.sm.cid.ui.components.showDatePicker
import com.studymate.sm.cid.viewmodel.AppViewModel

private val tipeAcara = listOf("LIBUR", "ACARA", "UJIAN", "DEADLINE")

private fun warnaTipe(tipe: String): Color = when (tipe) {
    "LIBUR" -> Color(0xFFD64545)
    "ACARA" -> Color(0xFFF4A623)
    "UJIAN" -> Color(0xFF1E3A5F)
    else -> Color(0xFF2E9E6D)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicCalendarScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val events by viewModel.calendarEvents.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedType by remember { mutableStateOf(tipeAcara[0]) }
    var typeMenuExpanded by remember { mutableStateOf(false) }

    FeatureScaffold(
        title = "Kalender Akademik",
        onMenuClick = onMenuClick,
        onAddClick = {
            title = ""; description = ""; date = System.currentTimeMillis(); selectedType = tipeAcara[0]
            showDialog = true
        }
    ) { padding ->
        if (events.isEmpty()) {
            EmptyState("Belum ada acara di kalender akademik.", padding)
        } else {
            val sorted = events.sortedBy { it.dateMillis }
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(sorted, key = { it.id }) { event ->
                    ItemCard(
                        title = event.title,
                        subtitle = "${event.type} • ${Formatters.date(event.dateMillis)}",
                        accentColor = warnaTipe(event.type),
                        onDelete = { viewModel.deleteCalendarEvent(event) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tambah Acara") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Nama acara") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    ExposedDropdownMenuBox(expanded = typeMenuExpanded, onExpandedChange = { typeMenuExpanded = it }) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        DropdownMenu(expanded = typeMenuExpanded, onDismissRequest = { typeMenuExpanded = false }) {
                            tipeAcara.forEach { t ->
                                DropdownMenuItem(text = { Text(t) }, onClick = { selectedType = t; typeMenuExpanded = false })
                            }
                        }
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedButton(onClick = { showDatePicker(context, date) { date = it } }, modifier = Modifier.fillMaxWidth()) {
                        Text("Tanggal: ${Formatters.date(date)}")
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Keterangan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        viewModel.saveCalendarEvent(
                            CalendarEvent(title = title.trim(), dateMillis = date, type = selectedType, description = description.trim())
                        )
                        showDialog = false
                    }
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal") } }
        )
    }
}
