package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.UserPreferences
import com.dennytech.data.remote.mapper.RemoteTokenMapper
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomainUser
import com.dennytech.data.remote.services.AuthService
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenPreferences: DataStore<UserPreferences>,
    private val preferenceRepository: PreferenceRepository,
    private val remoteTokenMapper: RemoteTokenMapper,
): AuthRepository {

    override suspend fun saveUser(entity: UserDomainModel) {
        tokenPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setEmail(entity.email)
                .setId(entity.id)
                .setStatus(entity.status)
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

            saveUser(user.toDomainUser())

            preferenceRepository.setAccessToken(user.token.orEmpty())
            preferenceRepository.setTokenExpiry(user.expiresIn ?: 0L) 

            user.toDomainUser()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun signup(request: HashMap<String, Any>): String {
        return try {

            val response =  authService.signup(request) 
            "Success"
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}