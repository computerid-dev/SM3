package com.studymate.sm.cid.backup

import com.studymate.sm.cid.data.CalendarEvent
import com.studymate.sm.cid.data.Category
import com.studymate.sm.cid.data.Exam
import com.studymate.sm.cid.data.FinanceEntry
import com.studymate.sm.cid.data.NoteItem
import com.studymate.sm.cid.data.ScheduleItem
import com.studymate.sm.cid.data.StudyTarget
import com.studymate.sm.cid.data.Subject
import com.studymate.sm.cid.data.SubjectMaterial
import com.studymate.sm.cid.data.TaskItem

/**
 * Fitur 7 & 8: Backup JSON / Import JSON.
 * Satu file JSON ini berisi SELURUH data aplikasi, jadi user bisa
 * pindah data ke HP lain atau balikin data setelah reset.
 */
data class AppBackup(
    val backupVersion: Int = 1,
    val appVersion: String = "v1.0.0",
    val exportedAtMillis: Long = System.currentTimeMillis(),
    val categories: List<Category> = emptyList(),
    val subjects: List<Subject> = emptyList(),
    val subjectMaterials: List<SubjectMaterial> = emptyList(),
    val scheduleItems: List<ScheduleItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList(),
    val financeEntries: List<FinanceEntry> = emptyList(),
    val exams: List<Exam> = emptyList(),
    val notes: List<NoteItem> = emptyList(),
    val calendarEvents: List<CalendarEvent> = emptyList(),
    val studyTargets: List<StudyTarget> = emptyList()
)
