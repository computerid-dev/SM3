package com.studymate.sm.cid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studymate.sm.cid.backup.AppBackup
import com.studymate.sm.cid.backup.BackupManager
import com.studymate.sm.cid.data.AppDatabase
import com.studymate.sm.cid.data.CalendarEvent
import com.studymate.sm.cid.data.Category
import com.studymate.sm.cid.data.Exam
import com.studymate.sm.cid.data.FinanceEntry
import com.studymate.sm.cid.data.NoteItem
import com.studymate.sm.cid.data.Repository
import com.studymate.sm.cid.data.ScheduleItem
import com.studymate.sm.cid.data.StudyTarget
import com.studymate.sm.cid.data.Subject
import com.studymate.sm.cid.data.SubjectMaterial
import com.studymate.sm.cid.data.TaskItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Satu ViewModel besar untuk seluruh aplikasi Study Mate.
 * Alasan dipakai satu ViewModel bersama (bukan satu-satu per layar):
 * datanya saling nyambung (contoh: Pelajaran dipakai di Tugas, Ujian,
 * Catatan, Jadwal, sampai Dashboard), jadi lebih simpel kalau satu sumber.
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(AppDatabase.getInstance(application))
    private val backupManager = BackupManager(application, repository)

    private fun <T> Flow<T>.asState(initial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initial)

    // ---------- STATE UNTUK SETIAP FITUR ----------
    val categories: StateFlow<List<Category>> = repository.categories.asState(emptyList())
    val subjects: StateFlow<List<Subject>> = repository.subjects.asState(emptyList())
    val schedule: StateFlow<List<ScheduleItem>> = repository.schedule.asState(emptyList())
    val tasks: StateFlow<List<TaskItem>> = repository.tasks.asState(emptyList())
    val financeEntries: StateFlow<List<FinanceEntry>> = repository.financeEntries.asState(emptyList())
    val exams: StateFlow<List<Exam>> = repository.exams.asState(emptyList())
    val notes: StateFlow<List<NoteItem>> = repository.notes.asState(emptyList())
    val calendarEvents: StateFlow<List<CalendarEvent>> = repository.calendarEvents.asState(emptyList())
    val studyTargets: StateFlow<List<StudyTarget>> = repository.studyTargets.asState(emptyList())

    fun materialsForSubject(subjectId: Long) = repository.materialsForSubject(subjectId)
    fun subjectById(id: Long) = repository.subjectById(id)
    fun scheduleForSubject(subjectId: Long) = repository.scheduleForSubject(subjectId)

    // ---------- KATEGORI ----------
    fun saveCategory(category: Category) = viewModelScope.launch { repository.saveCategory(category) }
    fun deleteCategory(category: Category) = viewModelScope.launch { repository.deleteCategory(category) }

    // ---------- PELAJARAN ----------
    fun saveSubject(subject: Subject, onSaved: (Long) -> Unit = {}) = viewModelScope.launch {
        val id = repository.saveSubject(subject)
        onSaved(id)
    }
    fun updateSubject(subject: Subject) = viewModelScope.launch { repository.updateSubject(subject) }
    fun deleteSubject(subject: Subject) = viewModelScope.launch { repository.deleteSubject(subject) }

    // ---------- MATERI PELAJARAN ----------
    fun saveMaterial(material: SubjectMaterial) = viewModelScope.launch { repository.saveMaterial(material) }
    fun deleteMaterial(material: SubjectMaterial) = viewModelScope.launch { repository.deleteMaterial(material) }

    // ---------- JADWAL ----------
    fun saveScheduleItem(item: ScheduleItem) = viewModelScope.launch { repository.saveScheduleItem(item) }
    fun deleteScheduleItem(item: ScheduleItem) = viewModelScope.launch { repository.deleteScheduleItem(item) }

    // ---------- TUGAS / PR ----------
    fun saveTask(task: TaskItem) = viewModelScope.launch { repository.saveTask(task) }
    fun toggleTaskDone(task: TaskItem) = viewModelScope.launch {
        repository.updateTask(task.copy(isDone = !task.isDone))
    }
    fun deleteTask(task: TaskItem) = viewModelScope.launch { repository.deleteTask(task) }

    // ---------- KEUANGAN ----------
    fun saveFinanceEntry(entry: FinanceEntry) = viewModelScope.launch { repository.saveFinanceEntry(entry) }
    fun deleteFinanceEntry(entry: FinanceEntry) = viewModelScope.launch { repository.deleteFinanceEntry(entry) }

    // ---------- UJIAN ----------
    fun saveExam(exam: Exam) = viewModelScope.launch { repository.saveExam(exam) }
    fun deleteExam(exam: Exam) = viewModelScope.launch { repository.deleteExam(exam) }

    // ---------- CATATAN MATERI ----------
    fun saveNote(note: NoteItem) = viewModelScope.launch { repository.saveNote(note) }
    fun deleteNote(note: NoteItem) = viewModelScope.launch { repository.deleteNote(note) }

    // ---------- KALENDER AKADEMIK ----------
    fun saveCalendarEvent(event: CalendarEvent) = viewModelScope.launch { repository.saveCalendarEvent(event) }
    fun deleteCalendarEvent(event: CalendarEvent) = viewModelScope.launch { repository.deleteCalendarEvent(event) }

    // ---------- TARGET BELAJAR ----------
    fun saveStudyTarget(target: StudyTarget) = viewModelScope.launch { repository.saveStudyTarget(target) }
    fun toggleTargetDone(target: StudyTarget) = viewModelScope.launch {
        repository.updateStudyTarget(target.copy(isDone = !target.isDone))
    }
    fun deleteStudyTarget(target: StudyTarget) = viewModelScope.launch { repository.deleteStudyTarget(target) }

    // ---------- BACKUP & IMPORT JSON ----------
    fun exportBackup(uri: android.net.Uri, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        val result = backupManager.exportTo(uri)
        onResult(result.isSuccess, result.exceptionOrNull()?.message)
    }

    fun importBackup(uri: android.net.Uri, onResult: (Boolean, String?) -> Unit) = viewModelScope.launch {
        val readResult = backupManager.readFrom(uri)
        val backup: AppBackup? = readResult.getOrNull()
        if (backup == null) {
            onResult(false, readResult.exceptionOrNull()?.message ?: "File backup tidak valid")
            return@launch
        }
        val restoreResult = backupManager.restore(backup)
        onResult(restoreResult.isSuccess, restoreResult.exceptionOrNull()?.message)
    }
}
