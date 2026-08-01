package com.studymate.sm.cid.backup

import android.content.Context
import android.net.Uri
import com.google.gson.GsonBuilder
import com.studymate.sm.cid.data.Repository
import kotlinx.coroutines.flow.first

/**
 * Menjalankan proses baca/tulis file backup.
 * Uri didapat dari Storage Access Framework (ACTION_CREATE_DOCUMENT /
 * ACTION_OPEN_DOCUMENT) yang dipicu dari layar Pengaturan.
 */
class BackupManager(
    private val context: Context,
    private val repository: Repository
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    /** Ambil semua data terbaru lalu susun jadi satu objek AppBackup. */
    suspend fun buildBackup(): AppBackup {
        return AppBackup(
            categories = repository.categories.first(),
            subjects = repository.subjects.first(),
            subjectMaterials = repository.subjects.first().flatMap { subject ->
                repository.materialsForSubject(subject.id).first()
            },
            scheduleItems = repository.schedule.first(),
            tasks = repository.tasks.first(),
            financeEntries = repository.financeEntries.first(),
            exams = repository.exams.first(),
            notes = repository.notes.first(),
            calendarEvents = repository.calendarEvents.first(),
            studyTargets = repository.studyTargets.first()
        )
    }

    /** Tulis backup ke file JSON yang dipilih user (fitur 7). */
    suspend fun exportTo(uri: Uri): Result<Unit> = runCatching {
        val backup = buildBackup()
        val json = gson.toJson(backup)
        context.contentResolver.openOutputStream(uri)?.use { output ->
            output.write(json.toByteArray(Charsets.UTF_8))
        } ?: throw IllegalStateException("Tidak bisa membuka file tujuan backup")
    }

    /** Baca file JSON lalu kembalikan objek AppBackup (fitur 8, langkah 1). */
    suspend fun readFrom(uri: Uri): Result<AppBackup> = runCatching {
        val json = context.contentResolver.openInputStream(uri)?.use { input ->
            input.readBytes().toString(Charsets.UTF_8)
        } ?: throw IllegalStateException("Tidak bisa membaca file backup")
        gson.fromJson(json, AppBackup::class.java)
    }

    /** Terapkan data dari AppBackup ke database (fitur 8, langkah 2). */
    suspend fun restore(backup: AppBackup): Result<Unit> = runCatching {
        backup.categories.forEach { repository.saveCategory(it) }
        backup.subjects.forEach { repository.saveSubject(it) }
        backup.subjectMaterials.forEach { repository.saveMaterial(it) }
        backup.scheduleItems.forEach { repository.saveScheduleItem(it) }
        backup.tasks.forEach { repository.saveTask(it) }
        backup.financeEntries.forEach { repository.saveFinanceEntry(it) }
        backup.exams.forEach { repository.saveExam(it) }
        backup.notes.forEach { repository.saveNote(it) }
        backup.calendarEvents.forEach { repository.saveCalendarEvent(it) }
        backup.studyTargets.forEach { repository.saveStudyTarget(it) }
    }
}
