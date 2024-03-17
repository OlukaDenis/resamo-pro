package com.dennytech.domain.models

data class UserDomainModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val lastLogin: String,
    val email: String,
    val phone: String,
    val status: Int,
    val fullName: String,
    val role: Int,
)