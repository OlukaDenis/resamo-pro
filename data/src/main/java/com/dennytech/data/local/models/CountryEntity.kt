package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val countryCode: String,
    val name: String,
    val currency: String,
    val dialCode: String
)
