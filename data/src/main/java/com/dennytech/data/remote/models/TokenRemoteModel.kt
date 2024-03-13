package com.dennytech.data.remote.models

data class TokenResponseModel(
    val data: TokenRemoteModel
)
data class TokenRemoteModel(
    val email: String?,
    val id: String?,
    val name: String?,
    val pin: String?,
    val token: String?,
    val unread: Int?
)

