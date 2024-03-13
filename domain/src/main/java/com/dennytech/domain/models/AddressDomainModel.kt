package com.dennytech.domain.models
data class AddressDomainModel(
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