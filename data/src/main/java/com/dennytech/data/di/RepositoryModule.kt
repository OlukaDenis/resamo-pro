package com.dennytech.data.di

import com.dennytech.data.impl.AuthRepositoryImpl
import com.dennytech.data.impl.LocationRepositoryImpl
import com.dennytech.data.impl.PreferenceRepositoryImpl
import com.dennytech.data.impl.ProfileRepositoryImpl
import com.dennytech.data.impl.SyncRepositoryImpl
import com.dennytech.data.impl.ProductRepositoryImpl
import com.dennytech.data.impl.UtilRepositoryImpl
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.LocationRepository
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.SyncRepository
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindUtilRepository(impl: UtilRepositoryImpl): UtilRepository

    @Singleton
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun bindPreferenceRepository(impl: PreferenceRepositoryImpl): PreferenceRepository

    @Singleton
    @Binds
    fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository

    @Singleton
    @Binds
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Singleton
    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    fun bindTransactionsRepository(impl: ProductRepositoryImpl): ProductRepository

}