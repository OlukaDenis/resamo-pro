package com.dennytech.domain.usecases.user

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProductRepository
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.UtilRepository
import com.dennytech.domain.usecases.sales.CreateSaleUseCase
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
class CreateUserUseCaseTest {
    private lateinit var mockDispatcher: AppDispatcher
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockUtilRepository: UtilRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: CreateUserUseCase


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDispatcher = mockk()
        mockProfileRepository = mockk()
        mockUtilRepository = mockk()
        every { mockDispatcher.io } returns  testDispatcher
        useCase = CreateUserUseCase(mockDispatcher, mockUtilRepository, mockProfileRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should emit loading then success when correct params are provided`() = runTest {
        // Given
        val params = CreateUserUseCase.Param(
            firstName = "John",
            lastName = "Denis",
            phone = "0773047490",
            email = "denis@gmail.com",
            password = "1234"
        )
        val mockUser = mockk<UserDomainModel>()
        coEvery { mockProfileRepository.createUser(any()) } returns mockUser

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        Truth.assertThat(result[1]).isEqualTo(Resource.Success(mockUser))
        coVerify { mockProfileRepository.createUser(any()) }
        coVerify(exactly = 1) {
            mockProfileRepository.createUser(
                withArg { request ->
                    Truth.assertThat(request["firstName"]).isEqualTo("John")
                }
            )
        }
    }

    @Test
    fun `should emit loading then error when an error is thrown`() = runTest {
        // Given
        val params = CreateUserUseCase.Param(
            firstName = "John",
            lastName = "Denis",
            phone = "0773047490",
            email = "denis@gmail.com",
            password = "1234"
        )
        val error = "Error"
        coEvery { mockUtilRepository.getNetworkError(any()) } returns error
        coEvery { mockProfileRepository.createUser(any()) } throws Throwable(error)

        // When
        val result = useCase.invoke(params).toList()

        // Then
        Truth.assertThat(result).hasSize(2)
        Truth.assertThat(result[0]).isEqualTo(Resource.Loading)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
        Truth.assertThat(result[1]).isEqualTo(Resource.Error(error))
        coVerify { mockProfileRepository.createUser(any()) }
    }

    @Test(expected = Exception::class)
    fun `should throw any error when no params are provided`() = runTest {
        // Given

        // When
        useCase.invoke(null).toList()

        // Then
    }
}