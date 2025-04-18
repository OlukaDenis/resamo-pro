package com.dennytech.data.impl

import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.remote.services.AuthService
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val preferenceRepository: PreferenceRepository
): SyncRepository {
    override suspend fun refreshToken(): String {
        return try {

            val token = preferenceRepository.getAccessToken().first()
            val headers = HashMap<String, Any>().apply {
                this["Authorization"] = "Bearer $token"
            }

            Timber.d("Refreshing token: %s", token)

            val response = authService.refreshToken(headers)
            val data  = response.data;

            preferenceRepository.setAccessToken(data.refreshToken.orEmpty()) 
            preferenceRepository.setTokenExpiry(data.expiresIn ?: 0L) 

            data.refreshToken.orEmpty()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}