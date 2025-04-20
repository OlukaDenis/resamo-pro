package com.dennytech.domain.usecases.user

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetStoreUsersUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetStoreUsersUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProfileRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetStoreUsersUseCase(mockDispatcher, mockUtilRepository, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit success when invoked`() = runTest {
        // Given
        val mockUsers = mockk<List<UserDomainModel>>()
        coEvery { mockProfileRepository.fetchStoreUserList() } returns mockUsers

        // When
        val result = useCase.invoke().toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(mockUsers))
        coVerify { mockProfileRepository.fetchStoreUserList() }
    }

    @Test
    fun `should return an error message when an error is thrown provided`() = runTest {
        // Given
        val error = "Error occurred"
        coEvery { mockProfileRepository.fetchStoreUserList() } throws Exception(error)
        coEvery { mockUtilRepository.getNetworkError(any()) } returns error

        // When
        val result = useCase.invoke(null).toList()

        // Then
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat((result[1] as Resource.Error).exception).isEqualTo(error)
    }
}