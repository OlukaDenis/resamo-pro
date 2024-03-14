package com.dennytech.data.remote.models

data class RefreshTokenResponseModel(
    val data: RemoteRefreshTokenModel
)
data class RemoteRefreshTokenModel(
    val expiresIn: Long?,
    val refreshToken: String?
)