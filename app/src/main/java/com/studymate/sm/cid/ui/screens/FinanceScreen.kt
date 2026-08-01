package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.data.FinanceEntry
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val entries by viewModel.financeEntries.collectAsState()
    val saldo = entries.sumOf { if (it.isIncome) it.amount else -it.amount }

    var showDialog by remember { mutableStateOf(false) }
    var isIncome by remember { mutableStateOf(true) }
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    FeatureScaffold(
        title = "Keuangan Sekolah",
        onMenuClick = onMenuClick,
        onAddClick = { isIncome = true; amountText = ""; note = ""; showDialog = true }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Saldo Sangu", color = androidx.compose.ui.graphics.Color.White)
                    Text(
                        Formatters.rupiah(saldo),
                        color = androidx.compose.ui.graphics.Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (entries.isEmpty()) {
                EmptyState("Belum ada catatan keuangan.", Modifier.fillMaxSize())
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(entries, key = { it.id }) { entry ->
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(entry.note.ifBlank { if (entry.isIncome) "Uang masuk" else "Pengeluaran" })
                                    Text(
                                        Formatters.date(entry.dateMillis),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Text(
                                    (if (entry.isIncome) "+ " else "- ") + Formatters.rupiah(entry.amount),
                                    color = if (entry.isIncome) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.SemiBold
                                )
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
            title = { Text("Catat Transaksi") },
            text = {
                Column {
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = isIncome,
                            onClick = { isIncome = true },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) { Text("Masuk") }
                        SegmentedButton(
                            selected = !isIncome,
                            onClick = { isIncome = false },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) { Text("Keluar") }
                    }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                        label = { Text("Jumlah (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Keterangan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.saveFinanceEntry(
                            FinanceEntry(
                                dateMillis = System.currentTimeMillis(),
                                isIncome = isIncome,
                                amount = amount,
                                note = note.trim()
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
