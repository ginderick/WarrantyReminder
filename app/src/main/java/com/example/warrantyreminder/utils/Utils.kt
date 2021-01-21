package com.example.warrantyreminder.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun convertMillisToString(millis: Long): String {

        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.setTimeInMillis(millis)
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        return format.format(utc.time)

    }

    fun convertStringToMillis(dateFormat: String): Long{

        val date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(dateFormat)
        return date!!.time

    }

}