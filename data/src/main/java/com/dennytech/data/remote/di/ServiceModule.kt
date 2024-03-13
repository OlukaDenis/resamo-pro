package com.dennytech.data.remote.di

import com.dennytech.data.remote.qualifiers.AuthApiQualifier
import com.dennytech.data.remote.qualifiers.NormalApiQualifier
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.remote.services.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    internal fun provideApiService(
        @NormalApiQualifier retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideAuthService(
        @AuthApiQualifier retrofit: Retrofit
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}