package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.ui.components.FeatureScaffold

@Composable
fun DeveloperInfoScreen(onMenuClick: () -> Unit) {
    FeatureScaffold(title = "Info Developer", onMenuClick = onMenuClick) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Study Mate (SM)", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Teman Belajar Digital", color = MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(16.dp))

                    InfoRow("Developer", "Nugroho Y.R.")
                    InfoRow("Package Name", "com.studymate.sm.cid")
                    InfoRow("Versi Aplikasi", "1.0.0")
                    InfoRow("Dibangun dengan", "Kotlin + Jetpack Compose")

                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Study Mate dibuat untuk membantu pelajar mengatur pelajaran, tugas, ujian, jadwal, catatan, target belajar, sampai keuangan sangu sehari-hari, semuanya dalam satu aplikasi.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
    }
}
