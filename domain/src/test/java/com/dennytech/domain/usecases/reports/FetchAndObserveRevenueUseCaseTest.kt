package com.dennytech.domain.usecases.reports

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.SalesRepository
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
class FetchAndObserveRevenueUseCaseTest {

    private lateinit var useCase: FetchAndObserveRevenueUseCase
    private lateinit var salesRepository: SalesRepository
    private lateinit var utilRepository: UtilRepository
    private lateinit var preferences: PreferenceRepository
    private lateinit var dispatcher: AppDispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        salesRepository = mockk()
        utilRepository = mockk()
        preferences = mockk()
        dispatcher = mockk()
        every { dispatcher.io } returns testDispatcher
        useCase = FetchAndObserveRevenueUseCase(dispatcher, utilRepository, salesRepository, preferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching revenue, should emit Loading then cached data then remote data`() = runTest {
        // Given
        val cachedRevenue = 1000L
        val remoteRevenue = 2000L
        val params = FetchAndObserveRevenueUseCase.Param(startDate = null, endDate = null)

        coEvery { preferences.getRevenue() } returns flowOf(cachedRevenue)
        coEvery { salesRepository.fetchRevenue(any()) } returns remoteRevenue
        coEvery { preferences.setRevenue(remoteRevenue) } returns Unit

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(3)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedRevenue)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[2] as Resource.Success).data).isEqualTo(cachedRevenue)

        coVerify { preferences.getRevenue() }
        coVerify { salesRepository.fetchRevenue(any()) }
        coVerify { preferences.setRevenue(remoteRevenue) }
    }

    @Test
    fun `when remote fetch fails, should emit Loading then cached data then Error`() = runTest {
        // Given
        val cachedRevenue = 1000L
        val exception = Exception("Network error")
        val params = FetchAndObserveRevenueUseCase.Param(startDate = null, endDate = null)

        coEvery { preferences.getRevenue() } returns flowOf(cachedRevenue)
        coEvery { salesRepository.fetchRevenue(any()) } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedRevenue)
        assertThat(results[2]).isInstanceOf(Resource.Error::class.java)
        assertThat((results[2] as Resource.Error).exception).isEqualTo(exception.toString())

        coVerify { preferences.getRevenue() }
        coVerify { salesRepository.fetchRevenue(any()) }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when observing revenue, should emit updates from cache`() = runTest {
        // Given
        val cachedRevenue = 1000L
        val updatedRevenue = 1500L
        val remoteRevenue = 2000L
        val params = FetchAndObserveRevenueUseCase.Param(startDate = null, endDate = null)

        coEvery { preferences.getRevenue() } returns flowOf(cachedRevenue, updatedRevenue)
        coEvery { salesRepository.fetchRevenue(any()) } returns remoteRevenue
        coEvery { preferences.setRevenue(remoteRevenue) } returns Unit

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(4)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(cachedRevenue)
        assertThat(results[2]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[2] as Resource.Success).data).isEqualTo(cachedRevenue)
        assertThat(results[3]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[3] as Resource.Success).data).isEqualTo(updatedRevenue)

        coVerify { preferences.getRevenue() }
        coVerify { salesRepository.fetchRevenue(any()) }
        coVerify { preferences.setRevenue(remoteRevenue) }
    }
} 