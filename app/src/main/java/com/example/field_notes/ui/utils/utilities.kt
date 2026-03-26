package com.example.field_notes.ui.utils

fun formatDate(timestamp: Long): String {
    val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = timestamp
    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
    val month = calendar.getDisplayName(
        java.util.Calendar.MONTH,
        java.util.Calendar.SHORT,
        java.util.Locale.getDefault()
    )
    val year = calendar.get(java.util.Calendar.YEAR)
    return "$month $day, $year"
}