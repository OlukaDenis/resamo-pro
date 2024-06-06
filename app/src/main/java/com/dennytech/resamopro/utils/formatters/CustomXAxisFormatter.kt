package com.dennytech.resamopro.utils.formatters

import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisFormatter(
    private val values: Array<String>
): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()

        return if (index >= 0 && index < values.size) {
            values[index]
        } else {
            ""
        }
    }
}