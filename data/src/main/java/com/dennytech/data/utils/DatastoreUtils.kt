package com.dennytech.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dennytech.data.TokenPreferences
import com.dennytech.data.local.di.APP_PREFERENCES_NAME
import com.dennytech.data.local.serializer.TokenPreferenceSerializer

val Context.tokenProtoDataStore: DataStore<TokenPreferences> by dataStore(
    fileName = "token_pref_db",
    serializer = TokenPreferenceSerializer
)

val Context.appThemeDatastore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")

val IS_SYSTEM_DEFAULT  = booleanPreferencesKey("is_system_default")
