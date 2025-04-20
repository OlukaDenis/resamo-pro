package com.dennytech.domain.usecases.reports

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ReportDomainModel
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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FetchPopularProductTypesUseCaseTest {

    private lateinit var useCase: FetchPopularProductTypesUseCase
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
        useCase = FetchPopularProductTypesUseCase(dispatcher, utilRepository, reportsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching popular product types succeeds, should emit Loading then Success with data`() = runTest {
        // Given
        val expectedReports = listOf(
            ReportDomainModel(count = 12, amount = 1200, revenue = 20000, type = "test"),
            ReportDomainModel(count = 12, amount = 100, revenue = 500, type = "test"),
        )
        coEvery { reportsRepository.fetchPopularProductTypes() } returns expectedReports

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(expectedReports)

        coVerify { reportsRepository.fetchPopularProductTypes() }
    }

    @Test
    fun `when fetching popular product types fails, should emit Loading then Error`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { reportsRepository.fetchPopularProductTypes() } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Error::class.java)
        assertThat((results[1] as Resource.Error).exception).isEqualTo(exception.toString())

        coVerify { reportsRepository.fetchPopularProductTypes() }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when use case is executed, should emit Loading first`() = runTest {
        // Given
        val expectedReports = listOf(ReportDomainModel(count = 12, amount = 100, revenue = 500, type = "test"),)
        coEvery { reportsRepository.fetchPopularProductTypes() } returns expectedReports

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
    }
} 