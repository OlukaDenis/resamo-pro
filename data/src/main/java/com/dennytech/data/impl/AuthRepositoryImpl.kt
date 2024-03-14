package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.UserPreferences
import com.dennytech.data.remote.mapper.RemoteTokenMapper
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.data.remote.services.AuthService
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenPreferences: DataStore<UserPreferences>,
    private val preferenceRepository: PreferenceRepository,
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

    override suspend fun saveUser(entity: UserDomainModel) {
        tokenPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setEmail(entity.email)
                .setId(entity.id)
                .setPhone(entity.phone)
                .setFirstName(entity.firstName)
                .setLastName(entity.lastName)
                .setRole(entity.role)
                .build()
        }
    }

    override suspend fun login(request: HashMap<String, Any>): UserDomainModel {
        return try {

            val response = authService.login(request)
            val user  = response.data;

            runBlocking { saveUser(user.toDomain()) }

            runBlocking { preferenceRepository.setAccessToken(user.toDomain().token) }

            user.toDomain()
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
}