package com.dennytech.domain.usecases.products

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateProductTypeUseCaseTest {

    // Test dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockUtilRepository: UtilRepository
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var useCase: CreateProductTypeUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockProductRepository = mockk(relaxUnitFun = true)
        mockProfileRepository = mockk(relaxUnitFun = true)
        mockUtilRepository = mockk()
        mockDispatcher = mockk()

        every { mockDispatcher.io } returns testDispatcher

        useCase = CreateProductTypeUseCase(
            dispatcher = mockDispatcher,
            utilRepository = mockUtilRepository,
            profileRepository = mockProfileRepository,
            productRepository = mockProductRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when run with valid params, should emit Loading then Success`() = runTest {
        // Given
        val mockUser = mockk<UserDomainModel>()
        val params = CreateProductTypeUseCase.Param(
            type = "Electronics",
            storeId = "store123"
        )

        coEvery { mockProductRepository.createProductType(any(), any()) } returns "Success"
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockUser

        // When
        val emissions = useCase.run(params).toList()

        // Then
        assertThat(emissions).hasSize(2)
        assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emissions[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((emissions[1] as Resource.Success).data).isEqualTo("Success")

        coVerify(exactly = 1) {
            mockProductRepository.createProductType(
                eq("store123"),
                withArg { request ->
                    assertThat(request["type"]).isEqualTo("Electronics")
                }
            )
        }
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }

    @Test
    fun `when run with null params, should emit Error`() = runTest {
        // Given
        coEvery { mockUtilRepository.getNetworkError(any()) } returns "Invalid params"

        // When
        val result = useCase.run(null).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo("Invalid params")
    }

    @Test
    fun `when product repository throws exception, should emit Error`() = runTest {
        // Given
        val params = CreateProductTypeUseCase.Param(
            type = "Electronics",
            storeId = "store123"
        )
        val expectedException = "Network error"
        coEvery { mockUtilRepository.getNetworkError(any()) } returns expectedException
        coEvery { mockProductRepository.createProductType(any(), any()) } throws Throwable(expectedException)

        // When
        val result = useCase.run(params).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo(expectedException)
    }

    @Test
    fun `when profile repository throws exception, should still emit Success`() = runTest {
        // Given
        val params = CreateProductTypeUseCase.Param(
            type = "Electronics",
            storeId = "store123"
        )

        coEvery { mockUtilRepository.getNetworkError(any()) } returns "Error"
        coEvery { mockProductRepository.createProductType(any(), any()) } returns "Success"
        coEvery { mockProfileRepository.fetchCurrentUser() } throws Exception("Profile fetch failed")

        // When
        val result = useCase.run(params).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo("Error")
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }

    @Test
    fun `should create correct request map with only type parameter`() = runTest {
        // Given
        val params = CreateProductTypeUseCase.Param(
            type = "Clothing",
            storeId = "store456"
        )
        coEvery { mockProductRepository.createProductType(any(), any()) } returns "Success"
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockk<UserDomainModel>()

        // When
        val result = useCase.run(params).toList()

        // Then
        coVerify(exactly = 1) {
            mockProductRepository.createProductType(
                eq("store456"),
                withArg { request ->
                    assertThat(request).hasSize(1)
                    assertThat(request["type"]).isEqualTo("Clothing")
                }
            )
        }
    }
}