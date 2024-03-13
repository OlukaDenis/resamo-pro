package com.dennytech.domain.models

data class ProfileDomainModel(
    val id: String,
    val userId: String,
    val role: String,
    val mobileCountryCode: String,
    val mobileNumber: String,
    val name: String,
    val address: AddressDomainModel?,
    val paymentMethods: List<PaymentMethodDomainModel>,
    val linked: UserDomainModel?,
    val trulipayUser: Boolean
)