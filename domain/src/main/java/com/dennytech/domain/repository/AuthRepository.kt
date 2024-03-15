package com.dennytech.domain.repository

import com.dennytech.domain.models.TokenDomainModel
import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: HashMap<String, Any>): UserDomainModel
    suspend fun signup(request: HashMap<String, Any>): String
    suspend fun saveUser(entity: UserDomainModel)
}