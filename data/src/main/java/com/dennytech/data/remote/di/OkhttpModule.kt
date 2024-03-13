package com.dennytech.data.remote.di

import android.app.Application
import android.content.Context
import com.dennytech.data.BuildConfig
import com.dennytech.data.remote.interceptors.AuthenticationInterceptor
import com.dennytech.data.remote.qualifiers.AuthApiQualifier
import com.dennytech.data.remote.qualifiers.NormalApiQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OkhttpModule {

    val CACHE_FILE_SIZE: Long = 1028 * 10 * 10

    @Provides
    @Singleton
    internal fun provideFile(@ApplicationContext context: Context): File {
        return File(context.cacheDir, "app_client")
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    internal fun provideCache(file: File): Cache {
        return Cache(file, CACHE_FILE_SIZE)
    }

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor.Logger {
        return HttpLoggingInterceptor.Logger { message -> Timber.i(message) }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(
        logger: HttpLoggingInterceptor.Logger
    ): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger).apply {
            level = when (BuildConfig.BUILD_TYPE) {
                "debug" -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @NormalApiQualifier
    internal fun provideApiOkhttpClient(
        cache: Cache,
        interceptor: HttpLoggingInterceptor,
        authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            cache(cache)
            addInterceptor(interceptor)
            addInterceptor(authenticationInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @AuthApiQualifier
    internal fun provideAuthOkhttpClient(
        cache: Cache,
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            cache(cache)
            addInterceptor(interceptor)
        }
        return builder.build()
    }

}