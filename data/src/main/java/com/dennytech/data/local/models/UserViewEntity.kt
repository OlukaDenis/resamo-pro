package com.dennytech.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserViewEntity(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val userPaymentMethods: List<PaymentMethodEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val senders: List<ProfileEntity>

//    val paymentMethods: List<PaymentMethodEntity>,
//    val receivers: List<ProfileEntity>,
//    val senders: List<ProfileEntity>,
//    val address: AddressDomainEntity?,

)