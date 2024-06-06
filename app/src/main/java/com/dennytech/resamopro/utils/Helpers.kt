package com.dennytech.resamopro.utils

import android.os.Build
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.RequiresApi
import com.dennytech.resamopro.models.KeyValueModel
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.TimeZone


object Helpers {

    fun shoeTypes(): List<KeyValueModel> {
        val list = mutableListOf<KeyValueModel>().apply {
            this.add(KeyValueModel("", ""))
            this.add(KeyValueModel("sandal", "Sandals"))
            this.add(KeyValueModel("pump", "Pumps"))
            this.add(KeyValueModel("heel", "Heels"))
            this.add(KeyValueModel("sneaker", "Sneakers"))
            this.add(KeyValueModel("loafer", "Loafer"))
            this.add(KeyValueModel("boot", "Boots"))
            this.add(KeyValueModel("gentle", "Gentle"))
        }

        return list.toList()
    }

    fun String.productTypeValue(): String {
        val item = shoeTypes().filter { it.key == this }
        return if (item.isNotEmpty()) item[0].value else ""
    }

    fun millisecondsToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()

        val date = Date(millis)
        return formatter.format(date)
    }

    fun millisecondsToDate(millis: Long, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()

        val date = Date(millis)
        return formatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTimeInMillis(): Long {
        val currentTime = LocalTime.now()
        val midnight = LocalTime.MIDNIGHT
        return midnight.until(currentTime, ChronoUnit.MILLIS)
    }

    fun Double.formatCurrency(symbol: String? = "UGX"): String{
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance(symbol)

        return format.format(this)
    }

    fun String.formatDateTime(): String {
        return try {
            if (this.isEmpty()) return ""

            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.getDefault())
            val output = SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.getDefault())
            val date = input.parse(this) ?: throw Exception("wrong date")

            return output.format(date)    // format output
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun String.formatDate(): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.getDefault())
            val output = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
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

    fun String.capitalize(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    fun Long.toKiloBytes(): Int {
        return (this / 1024).toInt()
    }

    fun Long.toMegaBytes(): Int {
        return (this / 1024 / 1024).toInt()
    }

}