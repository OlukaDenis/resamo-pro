package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val token: String,
    val phone: String,
    val fullName: String,
    val role: Int,
)