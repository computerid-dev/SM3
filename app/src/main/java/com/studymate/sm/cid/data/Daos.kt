package com.studymate.sm.cid.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: Category): Long

    @Delete
    suspend fun delete(category: Category)
}

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun getAll(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    fun getById(id: Long): Flow<Subject?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subject: Subject): Long

    @Update
    suspend fun update(subject: Subject)

    @Delete
    suspend fun delete(subject: Subject)
}

@Dao
interface SubjectMaterialDao {
    @Query("SELECT * FROM subject_materials WHERE subjectId = :subjectId ORDER BY id DESC")
    fun getForSubject(subjectId: Long): Flow<List<SubjectMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(material: SubjectMaterial): Long

    @Delete
    suspend fun delete(material: SubjectMaterial)
}

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule_items ORDER BY day ASC, startTime ASC")
    fun getAll(): Flow<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_items WHERE subjectId = :subjectId ORDER BY day ASC, startTime ASC")
    fun getForSubject(subjectId: Long): Flow<List<ScheduleItem>>

    @Query("SELECT * FROM schedule_items WHERE day = :day ORDER BY startTime ASC")
    fun getForDay(day: String): Flow<List<ScheduleItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ScheduleItem): Long

    @Delete
    suspend fun delete(item: ScheduleItem)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY deadlineMillis ASC")
    fun getAll(): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskItem): Long

    @Update
    suspend fun update(task: TaskItem)

    @Delete
    suspend fun delete(task: TaskItem)
}

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance_entries ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<FinanceEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: FinanceEntry): Long

    @Delete
    suspend fun delete(entry: FinanceEntry)
}

@Dao
interface ExamDao {
    @Query("SELECT * FROM exams ORDER BY dateMillis ASC")
    fun getAll(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(exam: Exam): Long

    @Delete
    suspend fun delete(exam: Exam)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): Flow<List<NoteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: NoteItem): Long

    @Delete
    suspend fun delete(note: NoteItem)
}

@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events ORDER BY dateMillis ASC")
    fun getAll(): Flow<List<CalendarEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event: CalendarEvent): Long

    @Delete
    suspend fun delete(event: CalendarEvent)
}

@Dao
interface StudyTargetDao {
    @Query("SELECT * FROM study_targets ORDER BY id DESC")
    fun getAll(): Flow<List<StudyTarget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(target: StudyTarget): Long

    @Update
    suspend fun update(target: StudyTarget)

    @Delete
    suspend fun delete(target: StudyTarget)
}
