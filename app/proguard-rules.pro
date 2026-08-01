# Study Mate (SM) - ProGuard rules
# Minifikasi dimatikan secara default (isMinifyEnabled = false),
# file ini disiapkan untuk jaga-jaga kalau nanti mau diaktifkan.

-keep class com.studymate.sm.cid.data.** { *; }
-keep class com.studymate.sm.cid.backup.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
