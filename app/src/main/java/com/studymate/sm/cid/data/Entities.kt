package com.studymate.sm.cid.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Fitur 1: Kategori
 * Mengelompokkan data (Sekolah, Tugas, Ujian, Keuangan, Catatan, dst).
 * Kategori bisa ditambah / diedit / dihapus sendiri oleh user.
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val colorHex: String = "#1E3A5F"
)

/**
 * Fitur 2 & 16: Pelajaran + Detail Pelajaran (fitur utama)
 * Setiap pelajaran punya halaman detail sendiri: guru, kategori, jadwal, materi.
 */
@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val teacher: String = "",
    val categoryId: Long? = null,
    val notes: String = ""
)

/**
 * Bagian dari Fitur 16 (Detail Pelajaran): daftar materi per pelajaran.
 * Nama bab, nomor halaman buku, ringkasan, catatan tambahan.
 */
@Entity(tableName = "subject_materials")
data class SubjectMaterial(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val chapterName: String,
    val pageRange: String = "",
    val summary: String = "",
    val notes: String = ""
)

/**
 * Fitur 10: Jadwal Pelajaran
 * Hari, jam, mata pelajaran. Juga dipakai di halaman Detail Pelajaran.
 */
@Entity(tableName = "schedule_items")
data class ScheduleItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val day: String,       // Senin, Selasa, ...
    val startTime: String, // "07:00"
    val endTime: String    // "08:30"
)

/**
 * Fitur 4: PR / Tugas
 */
@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val subjectId: Long? = null,
    val deadlineMillis: Long,
    val isDone: Boolean = false,
    val notes: String = ""
)

/**
 * Fitur 5: Keuangan Sekolah (uang sangu)
 */
@Entity(tableName = "finance_entries")
data class FinanceEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateMillis: Long,
    val isIncome: Boolean, // true = uang masuk, false = pengeluaran
    val amount: Double,
    val note: String = ""
)

/**
 * Fitur 6: Ujian / Ulangan
 */
@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val subjectId: Long? = null,
    val dateMillis: Long,
    val materialCovered: String = ""
)

/**
 * Fitur 11: Catatan Materi (rangkuman belajar)
 */
@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val subjectId: Long? = null
)

/**
 * Fitur 12: Kalender Akademik (libur, acara sekolah, ujian, deadline)
 */
@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val dateMillis: Long,
    val type: String, // LIBUR, ACARA, UJIAN, DEADLINE
    val description: String = ""
)

/**
 * Fitur 13: Target Belajar
 */
@Entity(tableName = "study_targets")
data class StudyTarget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val deadlineMillis: Long? = null,
    val isDone: Boolean = false
)
