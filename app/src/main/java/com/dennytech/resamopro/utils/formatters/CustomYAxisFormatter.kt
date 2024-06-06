package com.dennytech.resamopro.utils.formatters

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class CustomYAxisFormatter: ValueFormatter() {

    private val format = DecimalFormat("###.#")
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()


//        return if (index >= 0 && index < values.size) {
//            values[index]
//        } else {
//            ""
//        }

        return when {
            index >= 1000000 -> format.format(value / 1000000) + "M"
            value >= 1000 -> format.format(value / 1000) + "K"
            else -> value.toString()
        }
    }
}