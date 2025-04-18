package com.dennytech.domain.usecases.auth

import com.dennytech.domain.dispacher.AppDispatcher
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.AuthRepository
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.StoreRepository
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
class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private lateinit var authRepository: AuthRepository
    private lateinit var utilRepository: UtilRepository
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var storeRepository: StoreRepository
    private lateinit var dispatcher: AppDispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        utilRepository = mockk()
        preferenceRepository = mockk()
        storeRepository = mockk()
        dispatcher = mockk()
        every { dispatcher.io } returns testDispatcher
        useCase = LoginUseCase(dispatcher, utilRepository, authRepository, preferenceRepository, storeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when login succeeds with admin user and default store, should emit Loading then Success`() = runTest {
        // Given
        val stores = listOf(StoreDomainModel(id = "store1", name = "Store 1"))
        val user = UserDomainModel(
            id = "user1",
            email = "admin@test.com",
            stores = stores,
            defaultStore = "store1",
            role = 0
        )
        val params = LoginUseCase.Param(email = "admin@test.com", password = "password")
        val loginMap = hashMapOf<String, Any>("email" to params.email, "password" to params.password)

        coEvery { authRepository.login(loginMap) } returns user
        coEvery { storeRepository.saveStores(stores) } returns Unit
        coEvery { preferenceRepository.setCurrentStore("store1") } returns Unit

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(user)

        coVerify { authRepository.login(loginMap) }
        coVerify { storeRepository.saveStores(stores) }
        coVerify { preferenceRepository.setCurrentStore("store1") }
    }

    @Test
    fun `when login succeeds with non-admin user, should set first store as current`() = runTest {
        // Given
        val stores = listOf(StoreDomainModel(id = "store1", name = "Store 1"))
        val user = UserDomainModel(
            id = "user1",
            email = "user@test.com",
            stores = stores,
            defaultStore = "",
            role = 1
        )
        val params = LoginUseCase.Param(email = "user@test.com", password = "password")
        val loginMap =  hashMapOf<String, Any>("email" to params.email, "password" to params.password)

        coEvery { authRepository.login(loginMap) } returns user
        coEvery { storeRepository.saveStores(stores) } returns Unit
        coEvery { preferenceRepository.setCurrentStore("store1") } returns Unit

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Success::class.java)
        assertThat((results[1] as Resource.Success).data).isEqualTo(user)

        coVerify { authRepository.login(loginMap) }
        coVerify { storeRepository.saveStores(stores) }
        coVerify { preferenceRepository.setCurrentStore("store1") }
    }

    @Test
    fun `when login fails, should emit Loading then Error`() = runTest {
        // Given
        val params = LoginUseCase.Param(email = "user@test.com", password = "password")
        val loginMap =  hashMapOf<String, Any>("email" to params.email, "password" to params.password)
        val exception = Exception("Network error")

        coEvery { authRepository.login(loginMap) } throws exception
        coEvery { utilRepository.getNetworkError(exception) } returns exception.toString()

        // When
        val results = useCase(params).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Error::class.java)
        assertThat((results[1] as Resource.Error).exception).isEqualTo(exception.toString())

        coVerify { authRepository.login(loginMap) }
        coVerify { utilRepository.getNetworkError(exception) }
    }

    @Test
    fun `when params are null, should emit Loading then Error`() = runTest {
        // Given
        val exception = Exception("Invalid params")
        coEvery { utilRepository.getNetworkError(any()) } returns exception.toString()

        // When
        val results = useCase(null).toList()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(results[1]).isInstanceOf(Resource.Error::class.java)
        assertThat((results[1] as Resource.Error).exception).isEqualTo(exception.toString())

        coVerify { utilRepository.getNetworkError(any()) }
    }
} 