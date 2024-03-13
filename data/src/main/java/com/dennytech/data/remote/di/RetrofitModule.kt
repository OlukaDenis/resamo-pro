package com.dennytech.data.remote.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.dennytech.data.BuildConfig
import com.dennytech.data.remote.qualifiers.AuthApiQualifier
import com.dennytech.data.remote.qualifiers.NormalApiQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @NormalApiQualifier
    internal fun provideApiRetrofit(
        @NormalApiQualifier client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        val builder = Retrofit.Builder()
        builder.apply {
            baseUrl(BuildConfig.BASE_URL)
            client(client)
            addConverterFactory(converterFactory)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @AuthApiQualifier
    internal fun provideAuthRetrofit(
        @AuthApiQualifier client: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        val builder = Retrofit.Builder()
        builder.apply {
            baseUrl(BuildConfig.BASE_URL)
            client(client)
            addConverterFactory(converterFactory)
        }
        return builder.build()
    }
}