package com.dennytech.data.remote.interceptors

import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
//    private val profileRepository: ProfileRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val model = runBlocking { preferenceRepository.getAccessToken().first() }
        val builder = if (model.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $model")
//                .addHeader("Accept", "application/json")
                .addHeader("Accept", "*/*")
        } else chain.request().newBuilder()

        return chain.proceed(builder.build())

//        val originalRequest = chain.request()
//        val token = runBlocking { preferenceRepository.getAccessToken().first() }
//        val expiryTime = runBlocking { preferenceRepository.getTokenExpiry().first() }
//
//        if (token.isNotEmpty() && expiryTime.isAccessTokenExpired()) {
//            // Make the token refresh request
//            val refreshedToken = runBlocking { profileRepository.refreshToken() }
//
//            if (refreshedToken.isNotEmpty()) {
//                // Create a new request with the refreshed access token
//                val newRequest = originalRequest.newBuilder()
//                    .header("Authorization", "Bearer $refreshedToken")
//                    .addHeader("Accept", "*/*")
//                    .build()
//
//                // Retry the request with the new access token
//                return chain.proceed(newRequest)
//            }
//        }
//
//        // Add the access token to the request header
//        val authorizedRequest = originalRequest.newBuilder()
//            .header("Authorization", "Bearer $token")
//            .addHeader("Accept", "*/*")
//            .build()
//
//        return chain.proceed(authorizedRequest)
    }
}