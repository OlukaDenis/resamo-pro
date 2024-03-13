package com.dennytech.data.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dennytech.data.TokenPreferences
import com.dennytech.data.utils.tokenProtoDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val APP_PREFERENCES_NAME = "app_pref"

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    internal fun provideTokenPreferenceDatastore(
        @ApplicationContext context: Context
    ): DataStore<TokenPreferences> = context.tokenProtoDataStore


    @Provides
    @Singleton
    internal fun provideSettingsPreference(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.appThemeDatastore

}

val Context.appThemeDatastore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

val ENVIRONMENT = stringPreferencesKey("environment")