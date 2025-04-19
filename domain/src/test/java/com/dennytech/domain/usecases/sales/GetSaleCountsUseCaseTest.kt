package com.dennytech.domain.usecases.sales

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
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
class GetSaleCountsUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockReportRepository: ReportsRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetSaleCountsUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockReportRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetSaleCountsUseCase(mockDispatcher, mockUtilRepository, mockReportRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = GetSaleCountsUseCase.Param(
            startDate = "2024-04-04",
            endDate = "2024-04-04"
        )
        val mockInsights = mockk<InsightCountsDomainModel>()
        coEvery { mockReportRepository.fetchCounts(any()) } returns mockInsights

        // When
        val result = useCase.invoke(params).toList()

        // Then
        assertThat(result[0]).isEqualTo(Resource.Loading)
        assertThat(result[1]).isEqualTo(Resource.Success(mockInsights))
        coVerify { mockReportRepository.fetchCounts(any()) }
    }


    @Test
    fun `should emit success when no default params are provided`() = runTest {
        // Given
        val mockInsights = mockk<InsightCountsDomainModel>()
        coEvery { mockReportRepository.fetchCounts(any()) } returns mockInsights

        // When
        val result = useCase.invoke().toList()

        // Then
        assertThat(result[0]).isEqualTo(Resource.Loading)
        assertThat(result[1]).isEqualTo(Resource.Success(mockInsights))
        coVerify { mockReportRepository.fetchCounts(any()) }
    }

    @Test
    fun `should return an error message when an error is thrown provided`() = runTest {
        // Given
        val error = "Error occurred"
        coEvery { mockReportRepository.fetchCounts(any()) } throws Exception(error)
        coEvery { mockUtilRepository.getNetworkError(any()) } returns error

        // When
        val result = useCase.invoke(null).toList()

        // Then
        assertThat(result[0]).isEqualTo(Resource.Loading)
        assertThat((result[1] as Resource.Error).exception).isEqualTo(error)
    }
}