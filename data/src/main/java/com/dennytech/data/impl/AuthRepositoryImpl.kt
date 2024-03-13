package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.TokenPreferences
import com.dennytech.data.local.mappers.TokenPreferenceMapper
import com.dennytech.data.remote.mapper.RemoteTokenMapper
import com.dennytech.data.remote.services.AuthService
import com.dennytech.domain.models.TokenDomainModel
import com.dennytech.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenPreferences: DataStore<TokenPreferences>,
    private val tokenPreferenceMapper: TokenPreferenceMapper,
    private val remoteTokenMapper: RemoteTokenMapper,
): AuthRepository {
    override suspend fun sendOtp(request: HashMap<String, Any>): Boolean {
        return try {
            val response = authService.sendToken(request)
            response.success!!
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun checkUsername(request: HashMap<String, Any>): Boolean {
        return try {
            val response = authService.checkUsername(request)
            response.data!!
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun joinWaitList(request: HashMap<String, Any>): Boolean {
        return try {
            val response = authService.joinWaitlist(request)
            response.success!!
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun saveToken(entity: TokenDomainModel) {
        tokenPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setToken(entity.token)
                .setExpiresIn(entity.expiresIn)
                .setUnread(entity.unread)
                .build()
        }
    }

    override suspend fun login(request: HashMap<String, Any>): TokenDomainModel {
        return try {

            val response = authService.login(request)

            runBlocking { saveToken(remoteTokenMapper.toDomain(response.data)) }

            remoteTokenMapper.toDomain(response.data)
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun signup(request: HashMap<String, Any>): String {
        return try {

            val response = authService.signup(request)

//            runBlocking { saveToken(remoteTokenMapper.toDomain(response.data)) }
            "Success"
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getToken(): Flow<TokenDomainModel> {
        return try {
            tokenPreferences.data.map { tokenPreferenceMapper.toDomain(it) }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}