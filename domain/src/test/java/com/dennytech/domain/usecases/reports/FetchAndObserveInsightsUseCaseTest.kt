package com.dennytech.domain.usecases.reports

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ReportsRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
class FetchAndObserveInsightsUseCaseTest {

    private lateinit var useCase: FetchAndObserveInsightsUseCase
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
        useCase = FetchAndObserveInsightsUseCase(dispatcher, utilRepository, reportsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching insights, should emit Loading then cached data then remote data`() = runTest {
        // Given
        val cachedInsights = InsightCountsDomainModel(profit = 10L, salesCount = 10L, salesTotal = 100L)
        val remoteInsights = InsightCountsDomainModel(profit = 200L, salesCount = 10L, salesTotal = 1000L)

        coEvery { reportsRepository.getInsights() } returns flowOf(cachedInsights)
        coEvery { reportsRepository.fetchCounts(any()) } returns remoteInsights
        coEvery { reportsRepository.saveInsightsToCache(remoteInsights) } returns Unit

        // When
        val results = useCase().toList()

        // Then
        assertThat(results).hasSize(3)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedInsights)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        
        coVerify { reportsRepository.getInsights() }
        coVerify { reportsRepository.fetchCounts(any()) }
        coVerify { reportsRepository.saveInsightsToCache(remoteInsights) }
    }

    @Test
    fun `when remote fetch fails, should emit Loading then cached data then Error`() = runTest {
        // Given
        val cachedInsights = InsightCountsDomainModel(profit = 100L, salesCount = 5L, salesTotal = 500L)
        val exception = Exception("Network error")
        
        coEvery { reportsRepository.getInsights() } returns flowOf(cachedInsights)
        coEvery { reportsRepository.fetchCounts(any()) } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase().toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedInsights)
        assertThat(results[2]).isInstanceOf(Resource.Error::class.java)
        
        coVerify { reportsRepository.getInsights() }
        coVerify { reportsRepository.fetchCounts(any()) }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when observing insights, should emit updates from cache`() = runTest {
        // Given
        val cachedInsights = InsightCountsDomainModel(profit = 100L, salesCount = 5L, salesTotal = 500L)
        val updatedInsights = InsightCountsDomainModel(profit = 150L, salesCount = 7L, salesTotal = 750L)
        
        coEvery { reportsRepository.getInsights() } returns flowOf(cachedInsights, updatedInsights)
        coEvery { reportsRepository.fetchCounts(any()) } returns cachedInsights
        coEvery { reportsRepository.saveInsightsToCache(cachedInsights) } returns Unit

        // When
        val results = useCase().toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedInsights)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[2] as Resource.Success).data).isEqualTo(cachedInsights)
        assertThat(results[3]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[3] as Resource.Success).data).isEqualTo(updatedInsights)
    }
} 