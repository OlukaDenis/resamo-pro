package com.dennytech.domain.repository

import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun fetchUser(request: HashMap<String, Any>): UserDomainModel
    fun getCurrentUser(): Flow<UserDomainModel?>
    suspend fun refreshToken(): String
}