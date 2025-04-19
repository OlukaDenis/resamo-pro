package com.dennytech.domain.usecases.account

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentUserUseCaseTest {

    // Mocks
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var useCase: GetCurrentUserUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk(relaxed = true)
        mockProfileRepository = mockk()
        every { mockDispatcher.io } returns testDispatcher
        useCase = GetCurrentUserUseCase(mockDispatcher, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when execute is called, should return current user from repository`() = runTest {
        // Given
        val expectedUser = UserDomainModel(
            id = "123",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe"
        )
        every { mockProfileRepository.getCurrentUser() } returns flowOf(expectedUser)

        // When
        val result = useCase()

        // Then
        assertThat(result).isEqualTo(expectedUser)
        verify { mockProfileRepository.getCurrentUser() }
    }

    @Test(expected = Exception::class)
    fun `when user is null, should throw exception`() = runTest {
        // Given
        every { mockProfileRepository.getCurrentUser() } returns flowOf(null)

        // When
        useCase()

        // Then - Expects Exception to be thrown
    }

    @Test
    fun `should accept null parameter`() = runTest {
        // Given
        val expectedUser = mockk<UserDomainModel>()
        every { mockProfileRepository.getCurrentUser() } returns flowOf(expectedUser)

        // When
        val result = useCase(null)

        // Then
        assertThat(result).isEqualTo(expectedUser)
    }

    @Test
    fun `should call first() on the flow`() = runTest {
        // Given
        val expectedUser = mockk<UserDomainModel>()
        val userFlow = flowOf(expectedUser)
        every { mockProfileRepository.getCurrentUser() } returns userFlow

        // When
        val user = useCase()

        // Then
        println(user)
        assertThat(user).isNotNull()
    }

    @Test(expected = Exception::class)
    fun `when flow is empty, should throw exception`() = runTest {
        // Given
        every { mockProfileRepository.getCurrentUser() } returns flowOf()

        // When
        useCase(Unit)

        // Then - Expects Exception to be thrown
    }
}