package com.dennytech.domain.usecases.sales

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.repository.SalesRepository
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
class GetRecentSalesUseCaseTest {

    private lateinit var useCase: GetRecentSalesUseCase
    private lateinit var salesRepository: SalesRepository
    private lateinit var utilRepository: UtilRepository
    private lateinit var dispatcher: AppDispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        salesRepository = mockk()
        utilRepository = mockk()
        dispatcher = mockk()
        every { dispatcher.io } returns testDispatcher
        useCase = GetRecentSalesUseCase(dispatcher, utilRepository, salesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetchRecentSales succeeds, should emit Loading then Success with sales list`() = runTest {
        // Given
        val expectedSales = listOf(
            SaleDomainModel(id = "1", sellingPrice = 100),
            SaleDomainModel(id = "2", sellingPrice = 200)
        )
        coEvery { salesRepository.fetchRecentSales() } returns expectedSales

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(expectedSales)
        coVerify { salesRepository.fetchRecentSales() }
    }

    @Test
    fun `when fetchRecentSales fails, should emit Loading then Error`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { salesRepository.fetchRecentSales() } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Error::class.java)
//        assertThat((results[1] as Resource.Error).exception).isEqualTo(exception)
        coVerify { salesRepository.fetchRecentSales() }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when use case is executed, should emit Loading first`() = runTest {
        // Given
        val expectedSales = listOf(SaleDomainModel(id = "1", sellingPrice = 100))
        coEvery { salesRepository.fetchRecentSales() } returns expectedSales

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
    }
} 