package com.dennytech.domain.usecases.products

import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.just
import com.dennytech.domain.base.BaseFlowUseCase
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class CreateProductCategoryUseCaseTest {

    // Test dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockUtilRepository: UtilRepository
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var useCase: CreateProductCategoryUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockProductRepository = mockk()
        mockProfileRepository = mockk()
        mockUtilRepository = mockk()
        mockDispatcher = mockk(relaxed = true)
        every { mockDispatcher.io } returns testDispatcher

        useCase = CreateProductCategoryUseCase(
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
//        val exception = Exception("Network error")
        val mockUser = mockk<UserDomainModel>()
        val params = CreateProductCategoryUseCase.Param(
            name = "Test Category",
            description = "Test Description",
            storeId = "store123"
        )

        coEvery { mockUtilRepository.getNetworkError(any()) } returns "Error occurred"
        coEvery { mockProductRepository.createProductCategory(any()) } returns "Success"
        coEvery { mockProfileRepository.fetchCurrentUser() } returns mockUser


        // Then
        val result = useCase(params).toList()

        assertThat((result[0])).isEqualTo(Resource.Loading)
        assertThat((result[1])).isEqualTo(Resource.Success("Success"))

        coVerify(exactly = 1) {
            mockProductRepository.createProductCategory(withArg {
                assertThat(it["name"]).isEqualTo("Test Category")
                assertThat(it["description"]).isEqualTo("Test Description")
                assertThat(it["storeId"]).isEqualTo("store123")
            })
        }
        coVerify(exactly = 1) { mockProfileRepository.fetchCurrentUser() }
    }

    @Test
    fun `when run with null params, should emit Error`() = runTest {
        // Given
        val expectedException = Exception("Invalid params")
        every { mockUtilRepository.getNetworkError(any()) } returns expectedException.toString()

        // When
        val result = useCase.run(null).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo(expectedException.toString())
    }

    @Test
    fun `when repository throws exception, should emit Error`() = runTest {
        // Given
        val params = CreateProductCategoryUseCase.Param(
            name = "Test Category",
            description = "Test Description",
            storeId = "store123"
        )
        val expectedException = Exception("Network error")
        every { mockUtilRepository.getNetworkError(expectedException) } returns expectedException.toString()
        coEvery { mockProductRepository.createProductCategory(any()) } throws expectedException

        // When
        val result = useCase.run(params).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo(expectedException.toString())
    }

    @Test
    fun `should extend BaseFlowUseCase`() {
        assertThat(useCase).isInstanceOf(BaseFlowUseCase::class.java)
    }
}