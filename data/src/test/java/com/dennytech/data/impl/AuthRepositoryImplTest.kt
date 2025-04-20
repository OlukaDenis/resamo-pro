package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.UserPreferences
import com.dennytech.data.remote.mapper.RemoteTokenMapper
import com.dennytech.data.remote.models.UserRemoteModel
import com.dennytech.data.remote.models.UserResponse
import com.dennytech.data.remote.services.AuthService
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authService: AuthService
    private lateinit var tokenPreferences: DataStore<UserPreferences>
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var remoteTokenMapper: RemoteTokenMapper

    @Before
    fun setup() {
        authService = mockk()
        tokenPreferences = mockk()
        preferenceRepository = mockk()
        remoteTokenMapper = mockk()
        
        authRepository = AuthRepositoryImpl(
            authService,
            tokenPreferences,
            preferenceRepository,
            remoteTokenMapper
        )
    }

    @Test
    fun `login should save user data and tokens when successful`() = runTest {
        // Given
        val request = hashMapOf<String, Any>("email" to "test@example.com", "password" to "password")
        val mockUser = UserRemoteModel(
            id = "123",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            status = 1,
            role = 1,
            token = "mock_token",
            expiresIn = 3600L
        )
        
        coEvery { authService.login(any()) } returns UserResponse(data = mockUser)
        coEvery { tokenPreferences.updateData(any()) } returns mockk()
        coEvery { preferenceRepository.setAccessToken(any()) } returns Unit
        coEvery { preferenceRepository.setTokenExpiry(any()) } returns Unit

        // When
        val result = authRepository.login(request)

        // Then
        assertThat(result.id).isEqualTo("123")
        assertThat(result.email).isEqualTo("test@example.com")
        coVerify { 
            tokenPreferences.updateData(any())
            preferenceRepository.setAccessToken("mock_token")
            preferenceRepository.setTokenExpiry(3600L)
        }
    }

    @Test
    fun `signup should return success message when successful`() = runTest {
        // Given
        val request = hashMapOf<String, Any>(
            "email" to "test@example.com",
            "password" to "password",
            "firstName" to "John",
            "lastName" to "Doe"
        )
        coEvery { authService.signup(request) } returns mockk()

        // When
        val result = authRepository.signup(request)

        // Then
        assertThat(result).isEqualTo("Success")
        coVerify { authService.signup(request) }
    }

    @Test
    fun `saveUser should update token preferences with user data`() = runTest {
        // Given
        val user = UserDomainModel(
            id = "123",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890",
            status = 1,
            role = 1
        )
        coEvery { tokenPreferences.updateData(any()) } returns mockk()

        // When
        authRepository.saveUser(user)

        // Then
        coVerify { 
            tokenPreferences.updateData(any())
        }
    }

    @Test(expected = Exception::class)
    fun `login should throw exception when service fails`() = runTest {
        // Given
        val request = hashMapOf<String, Any>("email" to "test@example.com", "password" to "password")
        coEvery { authService.login(request) } throws Exception("Login failed")

        // When
        authRepository.login(request)
    }

    @Test(expected = Exception::class)
    fun `signup should throw exception when service fails`() = runTest {
        // Given
        val request = hashMapOf<String, Any>(
            "email" to "test@example.com",
            "password" to "password",
            "firstName" to "John",
            "lastName" to "Doe"
        )
        coEvery { authService.signup(request) } throws Exception("Signup failed")

        // When
        authRepository.signup(request)
    }
} 