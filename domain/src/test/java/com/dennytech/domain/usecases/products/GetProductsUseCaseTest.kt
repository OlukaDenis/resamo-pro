package com.dennytech.domain.usecases.products

import androidx.paging.PagingData
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.repository.ProductRepository
import io.mockk.coEvery
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
import com.google.common.truth.Truth.*
import io.mockk.coVerify

import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetProductsUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProductRepository: ProductRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetProductsUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProductRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetProductsUseCase(mockDispatcher, mockProductRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should return paged data when correct params are provided`() = runTest {
        // Given
        val params = GetProductsUseCase.Param(
            brand = "mybrand",
            color = "white",
            type = "open",
            size = "40"
        )
        val mockPagedData = mockk<PagingData<ProductDomainModel>>()
        coEvery { mockProductRepository.fetchProducts(any()) } returns flowOf(mockPagedData)

        // When
        val result = useCase(params).toList()

        // Then
        assertThat(result).isNotNull()
        coVerify { mockProductRepository.fetchProducts(any()) }
    }


    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given
//        val mockPagedData = mockk<PagingData<ProductDomainModel>>()
//        coEvery { mockProductRepository.fetchProducts(any()) } returns flowOf(mockPagedData)

        // When
        useCase(null).toList()

        // Then
    }
}
