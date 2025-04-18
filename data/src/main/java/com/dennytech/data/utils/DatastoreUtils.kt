package com.dennytech.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dennytech.data.InsightCounts
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.di.APP_PREFERENCES_NAME
import com.dennytech.data.local.serializer.InsightCountsSerializer
import com.dennytech.data.local.serializer.UserPreferenceSerializer

val Context.userProtoDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "token_pref_db",
    serializer = UserPreferenceSerializer
)

val Context.insightCountsStore: DataStore<InsightCounts> by dataStore(
    fileName = "insights_pref_db",
    serializer = InsightCountsSerializer
)

val Context.appThemeDatastore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")

val ACCESS_TOKEN_KEY  = stringPreferencesKey("token")
val CURRENT_STORE_KEY  = stringPreferencesKey("current_store")
val TOKEN_EXPIRY_KEY  = longPreferencesKey("expiry")

val REVENUE_KEY = longPreferencesKey("revenue")
