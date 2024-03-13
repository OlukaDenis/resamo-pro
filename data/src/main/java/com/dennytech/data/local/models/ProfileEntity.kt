package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ProfileEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val role: String,
    val mobileCountryCode: String,
    val mobileNumber: String,
    val name: String,
    val trulipayUser: Boolean
)