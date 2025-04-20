package com.dennytech.domain.usecases.products

import android.net.Uri
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
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
class UpdateProductUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: UpdateProductUseCase


    // Test data
    private val testUri = mockk<Uri>()
    private val testProduct = mockk<ProductDomainModel>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProductRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = UpdateProductUseCase(mockDispatcher, mockUtilRepository, mockProductRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = UpdateProductUseCase.Param(
            productId = "testId",
            fileUri = testUri,
            brand = "TestBrand",
            name = "TestProduct",
            color = "Red",
            size = "XL",
            type = "TestType",
            price = "19.99",
            quantity = "10",
            categoryId = "cat123",
            damaged = null
        )
        val mockProduct = mockk<ProductDomainModel>()
        coEvery { mockProductRepository.updateProduct(any(), any(), any()) } returns mockProduct

        // When
        val result = useCase.invoke(params).toList()

        // Then
        assertThat(result[0]).isEqualTo(Resource.Loading)
        assertThat(result[1]).isEqualTo(Resource.Success(mockProduct))
        coVerify { mockProductRepository.updateProduct(any(), any(), any()) }
    }

    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given
//        val mockPagedData = mockk<PagingData<ProductDomainModel>>()
//        coEvery { mockProductRepository.fetchProducts(any()) } returns flowOf(mockPagedData)

        // When
        useCase.invoke(null).toList()

        // Then
    }
}