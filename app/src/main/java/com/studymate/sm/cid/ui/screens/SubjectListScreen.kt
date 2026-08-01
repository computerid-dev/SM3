package com.studymate.sm.cid.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studymate.sm.cid.ui.components.EmptyState
import com.studymate.sm.cid.ui.components.FeatureScaffold
import com.studymate.sm.cid.ui.components.ItemCard
import com.studymate.sm.cid.viewmodel.AppViewModel

@Composable
fun SubjectListScreen(
    viewModel: AppViewModel,
    onMenuClick: () -> Unit,
    onSubjectClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val subjects by viewModel.subjects.collectAsState()
    val categories by viewModel.categories.collectAsState()

    FeatureScaffold(title = "Pelajaran", onMenuClick = onMenuClick, onAddClick = onAddClick) { padding ->
        if (subjects.isEmpty()) {
            EmptyState("Belum ada pelajaran. Yuk tambahkan pelajaranmu!", padding)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
                items(subjects, key = { it.id }) { subject ->
                    val categoryName = categories.find { it.id == subject.categoryId }?.name
                    ItemCard(
                        title = subject.name,
                        subtitle = listOfNotNull(
                            subject.teacher.ifBlank { null },
                            categoryName
                        ).joinToString(" • ").ifBlank { null },
                        onClick = { onSubjectClick(subject.id) },
                        onDelete = { viewModel.deleteSubject(subject) }
                    )
                }
            }
        }
    }
}
