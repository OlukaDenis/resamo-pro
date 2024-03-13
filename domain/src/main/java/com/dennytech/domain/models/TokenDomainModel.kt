package com.dennytech.domain.models

data class TokenDomainModel(
    val token: String,
    val unread: Long,
    val expiresIn: Long
)