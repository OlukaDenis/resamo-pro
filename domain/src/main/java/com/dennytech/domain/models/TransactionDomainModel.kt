package com.dennytech.domain.models

data class TransactionDomainModel(
    val id: String,
    val type: String,
    val amount: Double,
    val status: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
    val declinedAt: String,
    val receiver: UserDomainModel?,
    val sender: UserDomainModel?,
    val feeQuote: FeeQuoteDomainModel?
)

data class FeeQuoteDomainModel(
    val sourceCurrency: String,
    val destinationCurrency: String,
    val rate: Double,
    val sourceAmount: Double,
    val destinationAmount: Double
)