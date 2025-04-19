package com.dennytech.domain.usecases.account

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchCurrentUserUseCaseTest {

    // Mocks
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var useCase: FetchCurrentUserUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk(relaxed = true)
        mockProfileRepository = mockk()
        every { mockDispatcher.io } returns testDispatcher
        useCase = FetchCurrentUserUseCase(mockDispatcher, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when execute is called, should fetch current user from repository`() = runTest {
        // Given
        val mockUser = UserDomainModel(
            id = "123",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockUser

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }

    @Test
    fun `when execute is called and repository succeeds, should complete normally`() = runTest {
        // Given
        val mockUser = UserDomainModel(
            id = "123",
            email = "test@example.com"
        )
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockUser

        // When
        useCase(Unit)

        // Then - No exception should be thrown
        assertThat(true).isTrue()
    }

    @Test
    fun `when execute is called and repository throws exception, should not propagate it`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        coEvery { mockProfileRepository.fetchCurrentUser() } throws expectedException

        // When
        useCase(Unit)

        // Then
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }

    @Test
    fun `should accept null parameter`() = runTest {
        // Given
        val mockUser = mockk<UserDomainModel>()
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockUser

        // When
        useCase(null)

        // Then
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }
}