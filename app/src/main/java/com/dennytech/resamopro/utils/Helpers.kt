package com.dennytech.resamopro.utils

import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Locale

object Helpers {

    fun millisecondsToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    fun Double.formatCurrency(symbol: String? = "USD"): String{
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance(symbol)

        return format.format(this)
    }

    fun String.formatDate(): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.getDefault())
            val output = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
            val date = input.parse(this) ?: throw Exception("wrong date")

            return output.format(date)    // format output
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun String.pickInitials(): String {
        val parts = this.split(" ")

        return if (parts.isNotEmpty() && parts.size > 1) {
            "${parts[0].first()}${ parts[1].first()}"
        } else {
            if (parts.size == 1) "${parts[0].first()}" else "TP"
        }
    }

    fun Long.isAccessTokenExpired(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val expiry = this * 1000

        return currentTimeMillis >= expiry
    }
}