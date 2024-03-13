package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "address",
//    foreignKeys = [
//        ForeignKey(
//            entity = UserEntity::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("userId"),
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = ProfileEntity::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("profileId"),
//            onDelete = ForeignKey.CASCADE
//        ),
//    ]
)
data class AddressEntity(
    @PrimaryKey
    val id: Long,
    val profileId: String,
    val userId: String,
    val address: String,
    val address2: String,
    val city: String,
    val countryCode: String,
    val postalCode: String,
    val province: String,
)