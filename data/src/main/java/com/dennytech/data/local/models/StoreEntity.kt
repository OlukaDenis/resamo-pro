package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dennytech.data.local.converters.StringListConverter

@Entity("store")
data class StoreEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    val status: Int,
    val location: String,
    val userId: String,
    @TypeConverters(StringListConverter::class)
    val brands: List<String>,
    @TypeConverters(StringListConverter::class)
    val productTypes: List<String>,
)