package com.dennytech.domain.repository

import kotlinx.coroutines.flow.Flow


interface PreferenceRepository {
    suspend fun setOnboardingComplete(value: Boolean)
    fun isOnboardingComplete(): Flow<Boolean>
    suspend fun setAccessToken(value: String);
    fun getAccessToken(): Flow<String>
    suspend fun setTokenExpiry(value: Long);
    fun getTokenExpiry(): Flow<Long>
    suspend fun setCurrentStore(value: String)
    fun getCurrentStore(): Flow<String>

    suspend fun setRevenue(value: Long)

    fun getRevenue(): Flow<Long>
}