package com.dennytech.data.local.di

import android.content.Context
import androidx.room.Room
import com.dennytech.data.BuildConfig
import com.dennytech.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val APP_DATABASE_DB = if(BuildConfig.DEBUG) "rsm.db" else "resamo.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        APP_DATABASE_DB
    )
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideStateDao(database: AppDatabase) = database.stateDao()

    @Singleton
    @Provides
    fun provideProvinceDao(database: AppDatabase) = database.provinceDao()

    @Singleton
    @Provides
    fun provideCountryDao(database: AppDatabase) = database.countryDao()

    @Singleton
    @Provides
    fun providePaymentMethodDao(database: AppDatabase) = database.paymentMethodDao()

    @Singleton
    @Provides
    fun provideProfileDao(database: AppDatabase) = database.profileDao()

    @Singleton
    @Provides
    fun provideAddressDao(database: AppDatabase) = database.addressDao()
}