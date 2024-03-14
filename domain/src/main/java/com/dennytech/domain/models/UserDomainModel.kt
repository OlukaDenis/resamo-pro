package com.dennytech.domain.models

data class UserDomainModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val fullName: String,
    val role: Int,
)