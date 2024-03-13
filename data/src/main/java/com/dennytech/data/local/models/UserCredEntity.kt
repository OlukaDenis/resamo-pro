package com.dennytech.data.local.models

import kotlinx.serialization.Serializable

@Serializable
data class UserCredEntity(
    val username: String? = null,
    val password: String? = null
)