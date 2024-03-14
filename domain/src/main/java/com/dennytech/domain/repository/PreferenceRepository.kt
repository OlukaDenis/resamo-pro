package com.dennytech.domain.repository

import kotlinx.coroutines.flow.Flow


interface PreferenceRepository {
    suspend fun setOnboardingComplete(value: Boolean)
    fun isOnboardingComplete(): Flow<Boolean>
    suspend fun setAccessToken(value: String);
    fun getAccessToken(): Flow<String>
}