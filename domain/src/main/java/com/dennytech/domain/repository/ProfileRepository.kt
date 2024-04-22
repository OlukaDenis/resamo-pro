package com.dennytech.domain.repository

import androidx.paging.PagingData
import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun fetchCurrentUser(): UserDomainModel
    suspend fun fetchUserList(): Flow<PagingData<UserDomainModel>>
    suspend fun createUser(request: HashMap<String, Any>): UserDomainModel
    suspend fun activate(userId: String): UserDomainModel
    suspend fun deactivate(userId: String): UserDomainModel
    fun getCurrentUser(): Flow<UserDomainModel?>
    suspend fun saveCurrentUser(user: UserDomainModel)
    suspend fun assignUserToStore(
        storeId: String,
        request: HashMap<String, Any>
    )
    suspend fun fetchUnassignedUsers(request: HashMap<String, Any>): List<UserDomainModel>
}