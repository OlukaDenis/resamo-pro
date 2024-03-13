package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_method",
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
data class PaymentMethodEntity(
    @PrimaryKey
    val id: String,
    val profileId: String,
    val userId: String,
    val accountNumber: String,
    val bankCode: String,
    val createdAt: String,
    val currency: String,
    val accountBank: String,
    val type: String,
    val updatedAt: String
)