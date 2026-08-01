package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.Formatters
import com.studymate.sm.cid.viewmodel.AppViewModel

@Composable
fun DashboardScreen(viewModel: AppViewModel, onMenuClick: () -> Unit) {
    val subjects by viewModel.subjects.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val finance by viewModel.financeEntries.collectAsState()
    val targets by viewModel.studyTargets.collectAsState()

    val tugasBelumSelesai = tasks.count { !it.isDone }
    val ujianMendatang = exams.count { it.dateMillis >= System.currentTimeMillis() }
    val saldo = finance.sumOf { if (it.isIncome) it.amount else -it.amount }
    val targetAktif = targets.count { !it.isDone }

    FeatureScaffold(title = "Dashboard", onMenuClick = onMenuClick) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Selamat belajar! 👋",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "Ini ringkasan aktivitas belajarmu hari ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard("Pelajaran", subjects.size.toString(), Modifier.weight(1f))
                    SummaryCard("Tugas Belum Selesai", tugasBelumSelesai.toString(), Modifier.weight(1f))
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard("Ujian Mendatang", ujianMendatang.toString(), Modifier.weight(1f))
                    SummaryCard("Target Aktif", targetAktif.toString(), Modifier.weight(1f))
                }
            }
            item {
                SummaryCard("Saldo Sangu", Formatters.rupiah(saldo), Modifier.fillMaxWidth())
            }
            item {
                Text("Tugas terdekat", style = MaterialTheme.typography.titleMedium)
            }
            items(tasks.filter { !it.isDone }.sortedBy { it.deadlineMillis }.take(5)) { task ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(task.name, fontWeight = FontWeight.Medium)
                        Text(
                            "Deadline: ${Formatters.date(task.deadlineMillis)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            if (tasks.none { !it.isDone }) {
                item {
                    Text(
                        "Semua tugas sudah beres, mantap! 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }
    }
}
