package com.dennytech.domain.usecases.reports

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
class FetchAndObserveSaleReportUseCaseTest {

    private lateinit var useCase: FetchAndObserveSaleReportUseCase
    private lateinit var reportsRepository: ReportsRepository
    private lateinit var utilRepository: UtilRepository
    private lateinit var dispatcher: AppDispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        reportsRepository = mockk()
        utilRepository = mockk()
        dispatcher = mockk()
        every { dispatcher.io } returns testDispatcher
        useCase = FetchAndObserveSaleReportUseCase(dispatcher, utilRepository, reportsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching sale reports, should emit Loading then cached data then remote data`() = runTest {
        // Given
        val cachedReports = listOf(
            SaleReportDomainModel(period = "04-24", count = 12),
            SaleReportDomainModel(period = "04-25", count = 100),
        )
        val remoteReports = listOf(
            SaleReportDomainModel(period = "04-24", count = 15),
            SaleReportDomainModel(period = "04-25", count = 122),
        )

        coEvery { reportsRepository.getSalesReport() } returns flowOf(cachedReports)
        coEvery { reportsRepository.fetchSalesByPeriod() } returns remoteReports
        coEvery { reportsRepository.saveSaleReportToCache(remoteReports) } returns Unit

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(3)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedReports)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[2] as Resource.Success).data).isEqualTo(cachedReports)

        coVerify { reportsRepository.getSalesReport() }
        coVerify { reportsRepository.fetchSalesByPeriod() }
        coVerify { reportsRepository.saveSaleReportToCache(remoteReports) }
    }

    @Test
    fun `when remote fetch fails, should emit Loading then cached data then Error`() = runTest {
        // Given
        val cachedReports = listOf(SaleReportDomainModel(period = "04-24", count = 100))
        val exception = Exception("Network error")

        coEvery { reportsRepository.getSalesReport() } returns flowOf(cachedReports)
        coEvery { reportsRepository.fetchSalesByPeriod() } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedReports)
        assertThat(results[2]).isInstanceOf(Resource.Error::class.java)
        assertThat((results[2] as Resource.Error).exception).isEqualTo(exception.toString())

        coVerify { reportsRepository.getSalesReport() }
        coVerify { reportsRepository.fetchSalesByPeriod() }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when observing sale reports, should emit updates from cache`() = runTest {
        // Given
        val cachedReports = listOf(SaleReportDomainModel(period = "04-24", count = 100))
        val updatedReports = listOf(SaleReportDomainModel(period = "04-24", count = 111))
        val remoteReports = listOf(SaleReportDomainModel(period = "04-24", count = 112))

        coEvery { reportsRepository.getSalesReport() } returns flowOf(cachedReports, updatedReports)
        coEvery { reportsRepository.fetchSalesByPeriod() } returns remoteReports
        coEvery { reportsRepository.saveSaleReportToCache(remoteReports) } returns Unit

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedReports)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[2] as Resource.Success).data).isEqualTo(cachedReports)
        assertThat(results[3]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[3] as Resource.Success).data).isEqualTo(updatedReports)

        coVerify { reportsRepository.getSalesReport() }
        coVerify { reportsRepository.fetchSalesByPeriod() }
        coVerify { reportsRepository.saveSaleReportToCache(remoteReports) }
    }
} 