package com.dennytech.domain.repository

import com.dennytech.domain.models.TokenDomainModel
import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(request: HashMap<String, Any>): Boolean
    suspend fun checkUsername(request: HashMap<String, Any>): Boolean
    suspend fun joinWaitList(request: HashMap<String, Any>): Boolean
    suspend fun login(request: HashMap<String, Any>): UserDomainModel
    suspend fun signup(request: HashMap<String, Any>): String
    suspend fun saveUser(entity: UserDomainModel)
}