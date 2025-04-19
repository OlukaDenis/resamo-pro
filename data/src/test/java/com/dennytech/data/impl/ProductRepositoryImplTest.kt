package com.dennytech.data.impl

import android.content.Context
import androidx.paging.PagingData
import com.dennytech.data.remote.datasource.ProductPagingSource
import com.dennytech.data.remote.models.GenericUserResponse
import com.dennytech.data.remote.models.RemoteProductModel
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ProductDomainModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private lateinit var mockApiService: ApiService
    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: android.content.ContentResolver

    @Before
    fun setup() {
        mockApiService = mockk()
        mockContext = mockk()
        mockContentResolver = mockk()

        every { mockContext.contentResolver } returns mockContentResolver

        repository = ProductRepositoryImpl(mockApiService, mockContext)
    }

    @Test
    fun `fetchProducts should return paging data flow`(): Unit = runTest {
        // Given
        val filters = hashMapOf<String, Any>()
        val mockPagingSource = mockk<ProductPagingSource>()
        val mockPagingData = mockk<PagingData<ProductDomainModel>>()

        coEvery { mockApiService.getProducts(any(), any()) } returns mockk {
            every { data } returns listOf(mockk())
        }

        // When
        val result = repository.fetchProducts(filters)

        // Then
        assertNotNull(result)
        result.first() // Verify flow can be collected
    }

    @Test
    fun `fetchRecentProducts should return list of products`() = runTest {
        // Given
        val mockProducts = listOf(
            RemoteProductModel(id = "prd1", name = "Test 1", price = 1000),
            RemoteProductModel(id = "prd2", name = "Test 2", price = 1500),
            RemoteProductModel(id = "prd3", name = "Test 3", price = 5000)
        )
        coEvery { mockApiService.getProducts(any(), any()) } returns mockk {
            every { data } returns mockProducts
        }

        // When
        val result = repository.fetchRecentProducts()

        // Then
        assertNotNull(result)
        assertEquals(mockProducts.size, result.size)
    }

    @Test
    fun `createProductSale should return status code`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()
        val expectedStatusCode = 200

        coEvery { mockApiService.createSale(any()) } returns mockk {
            every { statusCode } returns expectedStatusCode
        }

        // When
        val result = repository.createProductSale(request)

        // Then
        assertEquals(expectedStatusCode, result)
    }

    @Test
    fun `createProductType should return success message`() = runTest {
        // Given
        val storeId = "store123"
        val request = hashMapOf<String, Any>()

        coEvery {
            mockApiService.createProductType(
                any(),
                any()
            )
        } returns GenericUserResponse(data = Any())

        // When
        val result = repository.createProductType(storeId, request)

        // Then
        assertEquals("Success", result)
    }

    @Test
    fun `createProductCategory should return success message`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()

        coEvery { mockApiService.createCategory(any()) } returns GenericUserResponse(data = Any())

        // When
        val result = repository.createProductCategory(request)

        // Then
        assertEquals("Success", result)
    }
} 