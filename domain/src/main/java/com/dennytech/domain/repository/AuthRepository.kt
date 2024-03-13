package com.dennytech.domain.repository

import com.dennytech.domain.models.TokenDomainModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendOtp(request: HashMap<String, Any>): Boolean
    suspend fun checkUsername(request: HashMap<String, Any>): Boolean
    suspend fun joinWaitList(request: HashMap<String, Any>): Boolean
    suspend fun login(request: HashMap<String, Any>): TokenDomainModel
    suspend fun signup(request: HashMap<String, Any>): String
    suspend fun saveToken(entity: TokenDomainModel)
    fun getToken(): Flow<TokenDomainModel>
}