package com.dennytech.domain.usecases.sales

import androidx.paging.PagingData
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.repository.SalesRepository
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
class GetSalesUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockSalesRepository: SalesRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetSalesUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockSalesRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetSalesUseCase(mockDispatcher, mockSalesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should return paged data when correct params are provided`() = runTest {
        // Given
        val params = GetSalesUseCase.Param(
            startDate = "2024-04-04",
            endDate = "2024-04-04"
        )
        val mockPagedData = mockk<PagingData<SaleDomainModel>>()
        coEvery { mockSalesRepository.fetchSales(any()) } returns flowOf(mockPagedData)

        // When
        val result = useCase(params).toList()

        // Then
        Truth.assertThat(result).isNotNull()
        coVerify { mockSalesRepository.fetchSales(any()) }
    }


    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given

        // When
        useCase(null).toList()

        // Then
    }
}