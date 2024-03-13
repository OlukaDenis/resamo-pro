package com.dennytech.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileViewEntity(
    @Embedded
    val profile: ProfileEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val profilePaymentMethods: List<PaymentMethodEntity>
)