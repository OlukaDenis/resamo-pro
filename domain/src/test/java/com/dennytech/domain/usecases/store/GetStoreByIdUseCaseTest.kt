package com.dennytech.domain.usecases.store

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.StoreRepository
import com.dennytech.domain.repository.UtilRepository
import com.dennytech.domain.usecases.products.UpdateProductUseCase
import com.dennytech.domain.usecases.user.GetStoreUsersUseCase
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetStoreByIdUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockStoreRepository: StoreRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GetStoreByIdUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockStoreRepository = mockk()
        mockStoreRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = GetStoreByIdUseCase(mockDispatcher, mockStoreRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }


    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = GetStoreByIdUseCase.Param(
            storeId = "testId"
        )
        val mockProduct = mockk<StoreDomainModel>()
        coEvery { mockStoreRepository.getStoreById(any())} returns mockProduct

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0]).isEqualTo(mockProduct)
        coVerify { mockStoreRepository.getStoreById(any()) }
    }

    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given

        // When
        useCase.invoke(null).toList()

        // Then
    }
}