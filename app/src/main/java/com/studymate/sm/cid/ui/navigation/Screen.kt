package com.studymate.sm.cid.ui.navigation

/**
 * Daftar semua rute (halaman) yang ada di Study Mate.
 * Total 16 fitur sesuai rancangan awal.
 */
sealed class Screen(val route: String, val label: String) {
    // Fitur 3
    data object Dashboard : Screen("dashboard", "Dashboard")
    // Fitur 1
    data object Category : Screen("category", "Kategori")
    // Fitur 2
    data object SubjectList : Screen("subject_list", "Pelajaran")
    // Fitur 16 (dengan parameter id)
    data object SubjectDetail : Screen("subject_detail/{subjectId}", "Detail Pelajaran") {
        fun createRoute(subjectId: Long) = "subject_detail/$subjectId"
    }
    // Fitur 15
    data object SubjectEdit : Screen("subject_edit/{subjectId}", "Edit Pelajaran") {
        fun createRoute(subjectId: Long) = "subject_edit/$subjectId"
    }
    data object SubjectAdd : Screen("subject_add", "Tambah Pelajaran")
    // Fitur 4
    data object Tasks : Screen("tasks", "PR / Tugas")
    // Fitur 5
    data object Finance : Screen("finance", "Keuangan Sekolah")
    // Fitur 6
    data object Exams : Screen("exams", "Ujian / Ulangan")
    // Fitur 7 & 8 (dalam satu layar Pengaturan)
    data object Settings : Screen("settings", "Pengaturan")
    // Fitur 9
    data object DeveloperInfo : Screen("developer_info", "Info Developer")
    // Fitur 10
    data object Schedule : Screen("schedule", "Jadwal Pelajaran")
    // Fitur 11
    data object Notes : Screen("notes", "Catatan Materi")
    // Fitur 12
    data object AcademicCalendar : Screen("academic_calendar", "Kalender Akademik")
    // Fitur 13
    data object StudyTargets : Screen("study_targets", "Target Belajar")
}

val drawerMenuItems = listOf(
    Screen.Dashboard,
    Screen.SubjectList,
    Screen.Tasks,
    Screen.Exams,
    Screen.Schedule,
    Screen.Finance,
    Screen.Notes,
    Screen.AcademicCalendar,
    Screen.StudyTargets,
    Screen.Category,
    Screen.Settings,
    Screen.DeveloperInfo
)
