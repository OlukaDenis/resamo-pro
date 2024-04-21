package com.dennytech.data.local.converters

import androidx.room.TypeConverter

class StringListConverter {

     private val STRING_SEPARATOR = "-"
    @TypeConverter
    fun List<String>.toStringData() = this.joinToString(STRING_SEPARATOR)

    @TypeConverter
    fun String.toList() = this.split(STRING_SEPARATOR)
}