package com.dennytech.domain.usecases.reports

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.SalesRepository
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
class GetRevenueByPeriodUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockSalesRepository: SalesRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetRevenueByPeriodUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockSalesRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetRevenueByPeriodUseCase(mockDispatcher, mockUtilRepository, mockSalesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = GetRevenueByPeriodUseCase.Param(
            startDate = "2024-04-04",
            endDate = "2024-04-04"
        )
        coEvery { mockSalesRepository.fetchRevenue(any()) } returns 1000L

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(1000L))
        coVerify(exactly = 1) { mockSalesRepository.fetchRevenue(any()) }
        coVerify(exactly = 1) {
            mockSalesRepository.fetchRevenue(
                withArg { request ->
                    Truth.assertThat(request["startDate"]).isEqualTo("2024-04-04")
                }
            )
        }
    }


    @Test
    fun `should emit success when no default params are provided`() = runTest {
        // Given
        coEvery { mockSalesRepository.fetchRevenue(any()) } returns 2000L

        // When
        val result = useCase.invoke().toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(2000L))
        coVerify { mockSalesRepository.fetchRevenue(any()) }
    }

    @Test
    fun `should return an error message when an error is thrown provided`() = runTest {
        // Given
        val error = "Error occurred"
        coEvery { mockSalesRepository.fetchRevenue(any()) } throws Exception(error)
        coEvery { mockUtilRepository.getNetworkError(any()) } returns error

        // When
        val result = useCase.invoke(null).toList()

        // Then
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat((result[1] as Resource.Error).exception).isEqualTo(error)
    }
}