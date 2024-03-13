package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user",
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val mobileCountryCode: String,
    val mobileNumber: String,
    val faceIdEnabled: Boolean,
    val pinSetup: Boolean,
    val accountBalance: Double,
    val dob: String,
    val currency: String,
    val kycStatus: String,
    val profileImage: String
)