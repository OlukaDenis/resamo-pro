package com.dennytech.data.di

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.impl.AppDispatcherImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    @Singleton
    internal abstract fun bindDispatcher(impl: AppDispatcherImpl): AppDispatcher
}