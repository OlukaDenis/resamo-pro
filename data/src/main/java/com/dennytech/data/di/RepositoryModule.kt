package com.dennytech.data.di

import com.dennytech.data.impl.AuthRepositoryImpl
import com.dennytech.data.impl.PreferenceRepositoryImpl
import com.dennytech.data.impl.ProfileRepositoryImpl
import com.dennytech.data.impl.SyncRepositoryImpl
import com.dennytech.data.impl.ProductRepositoryImpl
import com.dennytech.data.impl.ReportsRepositoryImpl
import com.dennytech.data.impl.SalesRepositoryImpl
import com.dennytech.data.impl.StoreRepositoryImpl
import com.dennytech.data.impl.UtilRepositoryImpl
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.SyncRepository
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.StoreRepository
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
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Singleton
    @Binds
    fun bindSalesRepository(impl: SalesRepositoryImpl): SalesRepository

    @Singleton
    @Binds
    fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository


    @Singleton
    @Binds
    fun bindReportsRepository(impl: ReportsRepositoryImpl): ReportsRepository

}