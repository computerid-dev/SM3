package com.studymate.sm.cid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Category::class,
        Subject::class,
        SubjectMaterial::class,
        ScheduleItem::class,
        TaskItem::class,
        FinanceEntry::class,
        Exam::class,
        NoteItem::class,
        CalendarEvent::class,
        StudyTarget::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun subjectDao(): SubjectDao
    abstract fun subjectMaterialDao(): SubjectMaterialDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun taskDao(): TaskDao
    abstract fun financeDao(): FinanceDao
    abstract fun examDao(): ExamDao
    abstract fun noteDao(): NoteDao
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun studyTargetDao(): StudyTargetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studymate.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
