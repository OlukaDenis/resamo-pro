package com.dennytech.domain.usecases.products

import android.net.Uri
import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.UtilRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
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
import timber.log.Timber
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class CreateProductUseCaseTest {

    // Test dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private lateinit var mockProductRepository: ProductRepository
    private lateinit var mockUtilRepository: UtilRepository
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var useCase: CreateProductUseCase

    // Test data
    private val testUri = mockk<Uri>()
    private val testProduct = mockk<ProductDomainModel>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockProductRepository = mockk()
        mockUtilRepository = mockk()
        mockDispatcher = mockk()

        every { mockDispatcher.io } returns testDispatcher

        // Mock Timber to prevent actual logging
        mockkStatic(Timber::class)
//        every { Timber.e(any<Throwable>()) } returns Unit

        useCase = CreateProductUseCase(
            dispatcher = mockDispatcher,
            utilRepository = mockUtilRepository,
            productRepository = mockProductRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `when run with all params, should emit Loading then Success with product`() = runTest {
        // Given
        val params = CreateProductUseCase.Param(
            fileUri = testUri,
            brand = "TestBrand",
            name = "TestProduct",
            color = "Red",
            size = "XL",
            type = "TestType",
            price = "19.99",
            quantity = "10",
            categoryId = "cat123"
        )

        coEvery { mockProductRepository.createNewProduct(any(), any()) } returns testProduct

        // When
        val emissions = useCase.run(params).toList()

        // Then
        assertThat(emissions).hasSize(2)
        assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emissions[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((emissions[1] as Resource.Success).data).isEqualTo(testProduct)
    }

    @Test
    fun `when run with required params only, should create correct request`() = runTest {
        // Given
        val params = CreateProductUseCase.Param(
            fileUri = testUri,
            brand = "TestBrand",
            name = "TestProduct",
            color = null,
            size = null,
            type = "TestType",
            price = "19.99",
            quantity = "10",
            categoryId = "cat123"
        )

        coEvery { mockProductRepository.createNewProduct(any(), any()) } returns testProduct
        val requestSlot = slot<HashMap<String, String>>()

        // When
        useCase.run(params).toList()

        // Then
        coVerify(exactly = 1) {
            mockProductRepository.createNewProduct(
                eq(testUri),
                capture(requestSlot)
            )
        }

        val request = requestSlot.captured
        assertThat(request).hasSize(6)
        assertThat(request).containsEntry("brand", "TestBrand")
        assertThat(request).containsEntry("name", "TestProduct")
        assertThat(request).containsEntry("type", "TestType")
        assertThat(request).containsEntry("price", "19.99")
        assertThat(request).containsEntry("quantity", "10")
        assertThat(request).containsEntry("categoryId", "cat123")
        assertThat(request).doesNotContainKey("color")
        assertThat(request).doesNotContainKey("size")
    }

    @Test
    fun `when run with null params, should emit Error`() = runTest {
        // Given
        val expectedException = "Invalid params"
        coEvery { mockUtilRepository.getNetworkError(any()) } returns expectedException

        // When
        val result = useCase.run(null).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo(expectedException)
    }

    @Test
    fun `when repository throws exception, should emit Error`() = runTest {
        // Given
        val params = CreateProductUseCase.Param(
            fileUri = testUri,
            brand = "TestBrand",
            name = "TestProduct",
            color = "Blue",
            size = "M",
            type = "TestType",
            price = "29.99",
            quantity = "5",
            categoryId = "cat456"
        )
        val expectedException ="Network error"
        coEvery { mockUtilRepository.getNetworkError(any()) } returns  expectedException
        coEvery { mockProductRepository.createNewProduct(any(), any()) } throws Throwable(expectedException)

        // When
        val result = useCase.run(params).first { it is Resource.Error }

        // Then
        assertThat((result as Resource.Error).exception).isEqualTo(expectedException)
    }

    @Test
    fun `should include optional params when provided`() = runTest {
        // Given
        val params = CreateProductUseCase.Param(
            fileUri = testUri,
            brand = "TestBrand",
            name = "TestProduct",
            color = "Green",
            size = "L",
            type = "TestType",
            price = "39.99",
            quantity = "15",
            categoryId = "cat789"
        )

        coEvery { mockProductRepository.createNewProduct(any(), any()) } returns testProduct
        val requestSlot = slot<HashMap<String, String>>()

        // When
        useCase.run(params).toList()

        // Then
        coVerify {
            mockProductRepository.createNewProduct(
                eq(testUri),
                capture(requestSlot)
            )
        }

        val request = requestSlot.captured
        assertThat(request).containsEntry("color", "Green")
        assertThat(request).containsEntry("size", "L")
    }
}