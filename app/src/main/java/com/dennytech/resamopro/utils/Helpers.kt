package com.dennytech.resamopro.utils

import com.dennytech.resamopro.models.KeyValueModel
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Locale

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