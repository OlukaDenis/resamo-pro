package com.dennytech.domain.repository


interface SyncRepository {
    suspend fun refreshToken(): String
}