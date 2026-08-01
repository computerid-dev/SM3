# Study Mate (SM)

Aplikasi teman belajar untuk pelajar — dibangun dengan **Kotlin + Jetpack Compose + Room**.

- Developer: Nugroho Y.R.
- Package name: `com.studymate.sm.cid`
- Versi: 1.0.0

## Fitur (16 total)
1. Kategori
2. Pelajaran
3. Dashboard
4. PR / Tugas
5. Keuangan Sekolah
6. Ujian / Ulangan
7. Backup Data (JSON)
8. Import Data (JSON)
9. Info Developer
10. Jadwal Pelajaran
11. Catatan Materi
12. Kalender Akademik
13. Target Belajar
14. Pengaturan
15. Edit Pelajaran
16. Detail Pelajaran

## Struktur Proyek
```
app/src/main/java/com/studymate/sm/cid/
├── data/          # Entity, DAO, Room Database, Repository
├── backup/        # Model & logika export/import JSON
├── viewmodel/      # AppViewModel (satu ViewModel untuk semua fitur)
└── ui/
    ├── theme/       # Warna, tipografi, tema Compose
    ├── navigation/  # Daftar rute + NavHost + Drawer menu
    ├── screens/     # 16 layar fitur
    └── components/  # Komponen UI yang dipakai berulang
```

## Cara Build Lokal
1. Buka folder ini dengan **Android Studio** (versi terbaru, disarankan Koala/2024.1+).
2. Tunggu proses **Gradle Sync** selesai (butuh koneksi internet untuk unduh dependency pertama kali).
3. Jalankan lewat tombol Run, atau build APK manual:
   ```
   ./gradlew assembleDebug
   ```
   (Jika folder belum punya `gradlew`, jalankan `gradle wrapper` sekali di terminal
   yang sudah terpasang Gradle, baru `./gradlew` bisa dipakai.)
4. Hasil APK ada di `app/build/outputs/apk/debug/app-debug.apk`.

## Cara Build Otomatis lewat GitHub Actions
Workflow `.github/workflows/main.yml` akan otomatis:
1. Checkout kode
2. Setup JDK 17 & Android SDK
3. Build APK debug (`gradle assembleDebug`)
4. Upload hasil APK sebagai artifact yang bisa diunduh dari tab **Actions**

Trigger otomatis saat push/PR ke branch `main`, atau bisa dijalankan manual lewat
tombol **Run workflow** di tab Actions (workflow_dispatch).

## Database
Semua data disimpan lokal pakai **Room (SQLite)** di HP — tidak butuh internet
untuk pemakaian sehari-hari. Backup/Import JSON (fitur 7 & 8) dipakai kalau mau
pindah data ke HP lain atau jaga-jaga sebelum uninstall.

## Catatan
- Minimum Android: 7.0 (API 24)
- Ikon aplikasi sudah dipasang sesuai logo Study Mate yang disediakan.
