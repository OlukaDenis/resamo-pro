package com.dennytech.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.di.APP_PREFERENCES_NAME
import com.dennytech.data.local.serializer.UserPreferenceSerializer

val Context.userProtoDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "token_pref_db",
    serializer = UserPreferenceSerializer
)

val Context.appThemeDatastore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")

val ACCESS_TOKEN_KEY  = stringPreferencesKey("token")
