package com.dennytech.domain.usecases.user

import androidx.paging.PagingData
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetUsersUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetUsersUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProfileRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetUsersUseCase(mockDispatcher, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should return paged data when correct params are provided`() = runTest {
        // Given
        val mockPagedData = mockk<PagingData<UserDomainModel>>()
        coEvery { mockProfileRepository.fetchUserList() } returns flowOf(mockPagedData)

        // When
        val result = useCase().toList()

        // Then
        Truth.assertThat(result).isNotNull()
        coVerify { mockProfileRepository.fetchUserList() }
    }

}