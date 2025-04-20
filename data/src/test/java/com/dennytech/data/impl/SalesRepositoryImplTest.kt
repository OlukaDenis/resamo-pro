package com.dennytech.data.impl

import com.dennytech.data.remote.models.ConfirmSaleResponse
import com.dennytech.data.remote.models.RemoteRevenueModel
import com.dennytech.data.remote.models.RemoteRevenueResponse
import com.dennytech.data.remote.models.RemoteSaleListResponse
import com.dennytech.data.remote.models.RemoteSaleModel
import com.dennytech.data.remote.models.RemoteSaleModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SalesRepositoryImplTest {

    private lateinit var repository: SalesRepositoryImpl
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = mockk()
        repository = SalesRepositoryImpl(apiService)
    }

    @Test
    fun `fetchSales should return paging data flow`() = runTest {
        // Given
        val filters = hashMapOf<String, Any>()

        coEvery { apiService.fetchSales(any()) } returns mockk {
            every { data } returns listOf(mockk())
        }


        // When
        val result = repository.fetchSales(filters)

        // Then
        Assert.assertNotNull(result)
        result.first()
    }

    @Test
    fun `fetchRecentSales should return list of sales`() = runTest {
        // Given
        val mockSales = listOf(
            RemoteSaleModel(id = "1", sellingPrice = 100),
            RemoteSaleModel(id = "2", sellingPrice = 200)
        )
        val expectedDomainSales = mockSales.map { it.toDomain() }

        coEvery { apiService.fetchSales(any()) } returns RemoteSaleListResponse(data = mockSales)

        // When
        val result = repository.fetchRecentSales()

        // Then
        assertEquals(expectedDomainSales.size, result.size)
        assertEquals(expectedDomainSales[0].id, result[0].id)
        assertEquals(expectedDomainSales[1].id, result[1].id)
    }

    @Test
    fun `fetchRevenue should return revenue amount`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()
        val expectedRevenue = RemoteRevenueModel(revenue = 10000L)

        coEvery { apiService.fetchRevenue(request) } returns RemoteRevenueResponse(data = expectedRevenue)

        // When
        val result = repository.fetchRevenue(request)

        // Then
        assertEquals(expectedRevenue.revenue, result)
    }

    @Test
    fun `confirmSale should return status code`() = runTest {
        // Given
        val saleId = "123"
        val expectedStatusCode = 200

        coEvery { apiService.confirmSale(saleId) } returns ConfirmSaleResponse(statusCode = expectedStatusCode)

        // When
        val result = repository.confirmSale(saleId)

        // Then
        assertEquals(expectedStatusCode, result)
    }

    @Test(expected = Exception::class)
    fun `fetchRecentSales should throw exception when API fails`() = runTest {
        // Given
        coEvery { apiService.fetchSales(any()) } throws Exception("API Error")

        // When
        repository.fetchRecentSales()
    }

    @Test(expected = Exception::class)
    fun `fetchRevenue should throw exception when API fails`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()
        coEvery { apiService.fetchRevenue(request) } throws Exception("API Error")

        // When
        repository.fetchRevenue(request)
    }

    @Test(expected = Exception::class)
    fun `confirmSale should throw exception when API fails`() = runTest {
        // Given
        val saleId = "123"
        coEvery { apiService.confirmSale(saleId) } throws Exception("API Error")

        // When
        repository.confirmSale(saleId)
    }
} 