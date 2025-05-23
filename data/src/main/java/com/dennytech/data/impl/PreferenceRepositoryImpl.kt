package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dennytech.data.utils.ACCESS_TOKEN_KEY
import com.dennytech.data.utils.CURRENT_STORE_KEY
import com.dennytech.data.utils.IS_ONBOARDING_COMPLETE
import com.dennytech.data.utils.REVENUE_KEY
import com.dennytech.data.utils.TOKEN_EXPIRY_KEY
import com.dennytech.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {
    override suspend fun setOnboardingComplete(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETE] = value
        }
    }

    override fun isOnboardingComplete(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[IS_ONBOARDING_COMPLETE]?: false
            }
    }

    override suspend fun setAccessToken(value: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = value
        }
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]?: ""
            }
    }

    override suspend fun setTokenExpiry(value: Long) {
        dataStore.edit { preferences ->
            preferences[TOKEN_EXPIRY_KEY] = value
        }
    }

    override fun getTokenExpiry(): Flow<Long> {
        return dataStore.data
            .map { preferences ->
                preferences[TOKEN_EXPIRY_KEY] ?: 0L
            }
    }

    override suspend fun setCurrentStore(value: String) {
        try {
            dataStore.edit { preferences ->
                preferences[CURRENT_STORE_KEY] = value
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getCurrentStore(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[CURRENT_STORE_KEY]?: ""
            }
    }


    override suspend fun setRevenue(value: Long) {
        dataStore.edit { it[REVENUE_KEY] = value }
    }

    override fun getRevenue(): Flow<Long> {
        return dataStore.data.map { it[REVENUE_KEY] ?: 0L }
    }
}