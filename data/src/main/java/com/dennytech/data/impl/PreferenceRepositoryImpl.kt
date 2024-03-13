package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dennytech.data.utils.IS_ONBOARDING_COMPLETE
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
}