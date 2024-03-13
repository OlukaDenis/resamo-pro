package com.dennytech.data.remote.interceptors

import com.dennytech.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val model = runBlocking { authRepository.getToken().first() }
        val builder = if (model.token.isNotEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${model.token}")
//                .addHeader("Accept", "application/json")
                .addHeader("Accept", "*/*")
        } else chain.request().newBuilder()

        return chain.proceed(builder.build())
    }
}