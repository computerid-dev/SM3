package com.studymate.sm.cid.ui.components

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    private val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
    }

    fun date(millis: Long): String = dateFormat.format(Date(millis))
    fun dateTime(millis: Long): String = dateTimeFormat.format(Date(millis))
    fun rupiah(amount: Double): String = rupiahFormat.format(amount)
}
