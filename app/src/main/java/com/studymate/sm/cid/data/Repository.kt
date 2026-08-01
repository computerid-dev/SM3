package com.studymate.sm.cid.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository = satu pintu akses data buat seluruh aplikasi.
 * ViewModel nggak perlu tahu soal Room / SQL, cukup panggil fungsi di sini.
 */
class Repository(private val db: AppDatabase) {

    // Kategori
    val categories: Flow<List<Category>> = db.categoryDao().getAll()
    suspend fun saveCategory(category: Category) = db.categoryDao().upsert(category)
    suspend fun deleteCategory(category: Category) = db.categoryDao().delete(category)

    // Pelajaran
    val subjects: Flow<List<Subject>> = db.subjectDao().getAll()
    fun subjectById(id: Long): Flow<Subject?> = db.subjectDao().getById(id)
    suspend fun saveSubject(subject: Subject) = db.subjectDao().upsert(subject)
    suspend fun updateSubject(subject: Subject) = db.subjectDao().update(subject)
    suspend fun deleteSubject(subject: Subject) = db.subjectDao().delete(subject)

    // Materi per pelajaran
    fun materialsForSubject(subjectId: Long): Flow<List<SubjectMaterial>> =
        db.subjectMaterialDao().getForSubject(subjectId)
    suspend fun saveMaterial(material: SubjectMaterial) = db.subjectMaterialDao().upsert(material)
    suspend fun deleteMaterial(material: SubjectMaterial) = db.subjectMaterialDao().delete(material)

    // Jadwal
    val schedule: Flow<List<ScheduleItem>> = db.scheduleDao().getAll()
    fun scheduleForSubject(subjectId: Long): Flow<List<ScheduleItem>> =
        db.scheduleDao().getForSubject(subjectId)
    fun scheduleForDay(day: String): Flow<List<ScheduleItem>> = db.scheduleDao().getForDay(day)
    suspend fun saveScheduleItem(item: ScheduleItem) = db.scheduleDao().upsert(item)
    suspend fun deleteScheduleItem(item: ScheduleItem) = db.scheduleDao().delete(item)

    // Tugas / PR
    val tasks: Flow<List<TaskItem>> = db.taskDao().getAll()
    suspend fun saveTask(task: TaskItem) = db.taskDao().upsert(task)
    suspend fun updateTask(task: TaskItem) = db.taskDao().update(task)
    suspend fun deleteTask(task: TaskItem) = db.taskDao().delete(task)

    // Keuangan
    val financeEntries: Flow<List<FinanceEntry>> = db.financeDao().getAll()
    suspend fun saveFinanceEntry(entry: FinanceEntry) = db.financeDao().upsert(entry)
    suspend fun deleteFinanceEntry(entry: FinanceEntry) = db.financeDao().delete(entry)

    // Ujian
    val exams: Flow<List<Exam>> = db.examDao().getAll()
    suspend fun saveExam(exam: Exam) = db.examDao().upsert(exam)
    suspend fun deleteExam(exam: Exam) = db.examDao().delete(exam)

    // Catatan Materi
    val notes: Flow<List<NoteItem>> = db.noteDao().getAll()
    suspend fun saveNote(note: NoteItem) = db.noteDao().upsert(note)
    suspend fun deleteNote(note: NoteItem) = db.noteDao().delete(note)

    // Kalender Akademik
    val calendarEvents: Flow<List<CalendarEvent>> = db.calendarEventDao().getAll()
    suspend fun saveCalendarEvent(event: CalendarEvent) = db.calendarEventDao().upsert(event)
    suspend fun deleteCalendarEvent(event: CalendarEvent) = db.calendarEventDao().delete(event)

    // Target Belajar
    val studyTargets: Flow<List<StudyTarget>> = db.studyTargetDao().getAll()
    suspend fun saveStudyTarget(target: StudyTarget) = db.studyTargetDao().upsert(target)
    suspend fun updateStudyTarget(target: StudyTarget) = db.studyTargetDao().update(target)
    suspend fun deleteStudyTarget(target: StudyTarget) = db.studyTargetDao().delete(target)
}
