package com.dennytech.domain.models

data class UserDomainModel(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val mobileCountryCode: String,
    val mobileNumber: String,
    val faceIdEnabled: Boolean,
    val pinSetup: Boolean,
    val paymentMethods: List<PaymentMethodDomainModel>,
    val receivers: List<ProfileDomainModel>,
    val senders: List<ProfileDomainModel>,
    val accountBalance: Double,
    val address: AddressDomainModel?,
    val dob: String,
    val currency: String,
    val kycStatus: String,
    val profileImage: String
)