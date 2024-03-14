package com.dennytech.data.remote.interceptors

import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val preferenceRepository: PreferenceRepository
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
    }
}