package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.InsightCounts
import com.dennytech.data.local.dao.SaleReportDao
import com.dennytech.data.local.mappers.InsightCountMapper
import com.dennytech.data.local.mappers.SaleReportEntityMapper
import com.dennytech.data.local.models.SaleReportEntity
import com.dennytech.data.remote.models.RemoteReportModel
import com.dennytech.data.remote.models.RemoteReportModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleCountsModel
import com.dennytech.data.remote.models.RemoteSaleCountsModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleReportModel
import com.dennytech.data.remote.models.RemoteSaleReportModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ReportsRepositoryImplTest {

    private lateinit var repository: ReportsRepositoryImpl
    private lateinit var mockApiService: ApiService
    private lateinit var mockSalesReportDao: SaleReportDao
    private lateinit var mockSaleReportEntityMapper: SaleReportEntityMapper
    private lateinit var mockInsightCountPreference: DataStore<InsightCounts>
    private lateinit var mockInsightCountMapper: InsightCountMapper

    @Before
    fun setup() {
        mockApiService = mockk()
        mockSalesReportDao = mockk()
        mockSaleReportEntityMapper = mockk()
        mockInsightCountPreference = mockk()
        mockInsightCountMapper = mockk()

        repository = ReportsRepositoryImpl(
            mockApiService,
            mockSalesReportDao,
            mockSaleReportEntityMapper,
            mockInsightCountPreference,
            mockInsightCountMapper
        )
    }

    @Test
    fun `fetchSalesByPeriod should return list of sale reports`() = runTest {
        // Given
        val mockReports = listOf(
            RemoteSaleReportModel(period="4-24", total = 100),
            RemoteSaleReportModel(period="5-24", total = 200)
        )
        val mockDomainReports = listOf(
            SaleReportDomainModel(period="4-24", total = 100),
            SaleReportDomainModel(period="5-24", total = 200)
        )

        coEvery { mockApiService.getSalesByPeriod() } returns mockk {
            every { data } returns mockReports
        }

        // When
        val result = repository.fetchSalesByPeriod()

        // Then
        assertNotNull(result)
        assertEquals(mockDomainReports.size, result.size)
        assertEquals(mockDomainReports[0].count, result[0].count,)
        assertEquals(mockDomainReports[1].count, result[1].count)
    }

    @Test
    fun `saveSaleReportToCache should save reports to dao`() = runTest {
        // Given
        val mockReports = listOf(
            SaleReportDomainModel(period="4-24", total = 100),
            SaleReportDomainModel(period="5-24", total = 200)
        )
        val mockEntities = listOf(
            mockk<SaleReportEntity>(),
        )

        coEvery { mockSalesReportDao.clear() } just Runs
        every { mockSaleReportEntityMapper.toLocal(mockReports[0]) } returns mockEntities[0]
        coEvery { mockSalesReportDao.insert(any()) } returns 1L

        // When
        repository.saveSaleReportToCache(mockReports)

        // Then
        coVerify { mockSalesReportDao.clear() }
        coVerify { mockSalesReportDao.insert(mockEntities[0]) }
    }

    @Test
    fun `saveInsightsToCache should update insight counts preference`() = runTest {
        // Given
        val mockInsight = InsightCountsDomainModel(
            salesCount = 10,
            salesTotal = 1000,
            profit = 500
        )
        val mockUpdatedPref = mockk<InsightCounts>()

        coEvery { mockInsightCountPreference.updateData(any()) } returns mockUpdatedPref

        // When
        repository.saveInsightsToCache(mockInsight)

        // Then
        coVerify { mockInsightCountPreference.updateData(any()) }
    }

    @Test
    fun `getInsights should return insight counts domain model`() = runTest {
        // Given
        val mockInsight = mockk<InsightCounts>()
        val mockDomainInsight = mockk<InsightCountsDomainModel>()

        every { mockInsightCountPreference.data } returns flowOf(mockInsight)
        every { mockInsightCountMapper.toDomain(mockInsight) } returns mockDomainInsight

        // When
        val result = repository.getInsights()

        // Then
        assertNotNull(result)
        assertEquals(mockDomainInsight, result.first())
    }

    @Test
    fun `getSalesReport should return list of sale report domain models`() = runTest {
        // Given
        val mockEntities = listOf(mockk<SaleReportEntity>())
        val mockDomainReports = listOf(
            SaleReportDomainModel(period="4-24", total = 100)
        )

        every { mockSalesReportDao.get() } returns flowOf(mockEntities)
        every { mockSaleReportEntityMapper.toDomain(mockEntities[0]) } returns mockDomainReports[0]

        // When
        val result = repository.getSalesReport()

        // Then
        assertNotNull(result)
        val reports = result.first()
        assertEquals(mockDomainReports.size, reports.size)
        assertEquals(mockDomainReports[0].count, reports[0].count)
    }

    @Test
    fun `fetchPopularProductTypes should return list of report domain models`() = runTest {
        // Given
        val mockReports = listOf(
            RemoteReportModel(count =10, revenue = 500, type = "test", amount = 300)
        )
        val mockDomainReports = listOf(
            ReportDomainModel(count =10, revenue = 500, type = "test", amount = 300),
        )

        coEvery { mockApiService.getPopularProductTypes() } returns mockk {
            every { data } returns mockReports
        }

        // When
        val result = repository.fetchPopularProductTypes()

        // Then
        assertNotNull(result)
        assertEquals(mockDomainReports.size, result.size)
        assertEquals(mockDomainReports[0].revenue, result[0].revenue)
    }

    @Test
    fun `fetchCounts should return insight counts domain model`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()
        val mockCounts = RemoteSaleCountsModel(
            salesCount = 10,
            salesTotal = 1000,
            profit = 500
        )
        val mockDomainCounts = InsightCountsDomainModel(
            salesCount = 10,
            salesTotal = 1000,
            profit = 500
        )

        coEvery { mockApiService.fetchSaleCounts(request) } returns mockk {
            every { data } returns mockCounts
        }
//        every { mockCounts.toDomain() } returns mockDomainCounts

        // When
        val result = repository.fetchCounts(request)

        // Then
        assertNotNull(result)
        assertEquals(mockDomainCounts.salesCount, result.salesCount)
        assertEquals(mockDomainCounts.salesTotal, result.salesTotal)
        assertEquals(mockDomainCounts.profit, result.profit)
    }
} 