package com.dennytech.data.remote.interceptors

import com.dennytech.data.utils.isAccessTokenExpired
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.SyncRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val syncRepository: SyncRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val token = runBlocking { preferenceRepository.getAccessToken().first() }
        val expiryTime = runBlocking { preferenceRepository.getTokenExpiry().first() }

        val currentStore = runBlocking { preferenceRepository.getCurrentStore().first() }

        if (token.isNotEmpty() && expiryTime.isAccessTokenExpired()) {
            // Make the token refresh request
            val refreshedToken = runBlocking { syncRepository.refreshToken() }

            if (refreshedToken.isNotEmpty()) {
                // Create a new request with the refreshed access token
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $refreshedToken")
                    .header("x-store-id", currentStore)
                    .addHeader("Accept", "*/*")
                    .build()

                // Retry the request with the new access token
                return chain.proceed(newRequest)
            }
        }

        // Add the access token to the request header
        val authorizedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("x-store-id", currentStore)
            .addHeader("Accept", "*/*")
            .build()

        return chain.proceed(authorizedRequest)
    }
}