package com.dennytech.domain.models

data class PaymentMethodDomainModel(
    val id: String,
    val profileId: String,
    val userId: String,
    val accountNumber: String,
    val bankCode: String,
    val createdAt: String,
    val billingAddress: AddressDomainModel?,
    val currency: String,
    val accountBank: String,
    val type: String,
    val updatedAt: String
)