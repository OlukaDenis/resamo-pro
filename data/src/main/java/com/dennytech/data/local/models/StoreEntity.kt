package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("store")
data class StoreEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    val status: Int,
    val location: String,
    val userId: String
)