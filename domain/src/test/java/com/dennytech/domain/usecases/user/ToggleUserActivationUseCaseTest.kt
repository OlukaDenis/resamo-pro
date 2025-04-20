package com.dennytech.domain.usecases.user

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.SalesRepository
import com.dennytech.domain.repository.UtilRepository
import com.dennytech.domain.usecases.reports.GetRevenueByPeriodUseCase
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToggleUserActivationUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ToggleUserActivationUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProfileRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = ToggleUserActivationUseCase(mockDispatcher, mockUtilRepository, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit success when deactivating user`() = runTest {
        // Given
        val params = ToggleUserActivationUseCase.Param(
            userId = "testId",
            userStatus = 1
        )
        val mockUser = mockk<UserDomainModel>()
        coEvery { mockProfileRepository.deactivate(any()) } returns mockUser

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(mockUser))
        coVerify(exactly = 1) { mockProfileRepository.deactivate(any()) }
    }


    @Test
    fun `should emit success when activating user`() = runTest {
        // Given
        val params = ToggleUserActivationUseCase.Param(
            userId = "testId",
            userStatus = 0
        )
        val mockUser = mockk<UserDomainModel>()
        coEvery { mockProfileRepository.activate(any()) } returns mockUser

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(mockUser))
        coVerify(exactly = 1) { mockProfileRepository.activate(any()) }
    }

    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given

        // When
        useCase(null).toList()

        // Then
    }
}