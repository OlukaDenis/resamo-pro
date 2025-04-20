package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import com.dennytech.data.utils.ACCESS_TOKEN_KEY
import com.dennytech.data.utils.CURRENT_STORE_KEY
import com.dennytech.data.utils.IS_ONBOARDING_COMPLETE
import com.dennytech.data.utils.REVENUE_KEY
import com.dennytech.data.utils.TOKEN_EXPIRY_KEY
import com.google.common.truth.Truth.assertThat
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PreferenceRepositoryImplTest {

    private lateinit var preferenceRepository: PreferenceRepositoryImpl
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var preferences: Preferences

    @Before
    fun setup() {
        dataStore = mockk(relaxed = true)
        preferences = mockk(relaxed = true)
        
        every { dataStore.data } returns flowOf(preferences)
        
        preferenceRepository = PreferenceRepositoryImpl(dataStore)
    }

    @Test
    fun `setOnboardingComplete should update preferences`() = runTest {
        // Given
        val value = true

        // When
        preferenceRepository.setOnboardingComplete(value)

        // Then
        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `isOnboardingComplete should return true when set`() = runTest {
        // Given
        every { preferences[IS_ONBOARDING_COMPLETE] } returns true

        // When
        val result = preferenceRepository.isOnboardingComplete().first()

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `isOnboardingComplete should return false when not set`() = runTest {
        // Given
        every { preferences[IS_ONBOARDING_COMPLETE] } returns null

        // When
        val result = preferenceRepository.isOnboardingComplete().first()

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `setAccessToken should update preferences`() = runTest {
        // Given
        val token = "test_token"

        // When
        preferenceRepository.setAccessToken(token)

        // Then
        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `getAccessToken should return token when set`() = runTest {
        // Given
        val token = "test_token"
        every { preferences[ACCESS_TOKEN_KEY] } returns token

        // When
        val result = preferenceRepository.getAccessToken().first()

        // Then
        assertThat(result).isEqualTo(token)
    }

    @Test
    fun `getAccessToken should return empty string when not set`() = runTest {
        // Given
        every { preferences[ACCESS_TOKEN_KEY] } returns null

        // When
        val result = preferenceRepository.getAccessToken().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `setTokenExpiry should update preferences`() = runTest {
        // Given
        val expiry = 3600L

        // When
        preferenceRepository.setTokenExpiry(expiry)

        // Then
        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `getTokenExpiry should return expiry when set`() = runTest {
        // Given
        val expiry = 3600L
        every { preferences[TOKEN_EXPIRY_KEY] } returns expiry

        // When
        val result = preferenceRepository.getTokenExpiry().first()

        // Then
        assertThat(result).isEqualTo(expiry)
    }

    @Test
    fun `getTokenExpiry should return 0 when not set`() = runTest {
        // Given
        every { preferences[TOKEN_EXPIRY_KEY] } returns null

        // When
        val result = preferenceRepository.getTokenExpiry().first()

        // Then
        assertThat(result).isEqualTo(0L)
    }

    @Test
    fun `setCurrentStore should update preferences`() = runTest {
        // Given
        val storeId = "store123"

        // When
        preferenceRepository.setCurrentStore(storeId)

        // Then
        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `getCurrentStore should return store ID when set`() = runTest {
        // Given
        val storeId = "store123"
        every { preferences[CURRENT_STORE_KEY] } returns storeId

        // When
        val result = preferenceRepository.getCurrentStore().first()

        // Then
        assertThat(result).isEqualTo(storeId)
    }

    @Test
    fun `getCurrentStore should return empty string when not set`() = runTest {
        // Given
        every { preferences[CURRENT_STORE_KEY] } returns null

        // When
        val result = preferenceRepository.getCurrentStore().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `setRevenue should update preferences`() = runTest {
        // Given
        val revenue = 1000L

        // When
        preferenceRepository.setRevenue(revenue)

        // Then
        coVerify(exactly = 1) { dataStore.updateData(any()) }
    }

    @Test
    fun `getRevenue should return revenue when set`() = runTest {
        // Given
        val revenue = 1000L
        every { preferences[REVENUE_KEY] } returns revenue

        // When
        val result = preferenceRepository.getRevenue().first()

        // Then
        assertThat(result).isEqualTo(revenue)
    }

    @Test
    fun `getRevenue should return 0 when not set`() = runTest {
        // Given
        every { preferences[REVENUE_KEY] } returns null

        // When
        val result = preferenceRepository.getRevenue().first()

        // Then
        assertThat(result).isEqualTo(0L)
    }
} 