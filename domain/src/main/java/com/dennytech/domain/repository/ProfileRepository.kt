package com.dennytech.domain.repository

import androidx.paging.PagingData
import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun fetchUser(request: HashMap<String, Any>): UserDomainModel
    suspend fun fetchUserList(): Flow<PagingData<UserDomainModel>>
    suspend fun createUser(request: HashMap<String, Any>): UserDomainModel
    suspend fun activate(userId: String): UserDomainModel
    suspend fun deactivate(userId: String): UserDomainModel
    fun getCurrentUser(): Flow<UserDomainModel?>
    suspend fun refreshToken(): String
}