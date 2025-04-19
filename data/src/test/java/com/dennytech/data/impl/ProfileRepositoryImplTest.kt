package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.paging.PagingData
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.mappers.UserPreferencesMapper
import com.dennytech.data.remote.datasource.UserPagingSource
import com.dennytech.data.remote.models.GenericUserResponse
import com.dennytech.data.remote.models.UserRemoteModel
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.StoreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {

    private lateinit var repository: ProfileRepositoryImpl
    private lateinit var mockApiService: ApiService
    private lateinit var mockUserPreferences: DataStore<UserPreferences>
    private lateinit var mockUserPreferencesMapper: UserPreferencesMapper
    private lateinit var mockStoreRepository: StoreRepository

    @Before
    fun setup() {
        mockApiService = mockk()
        mockUserPreferences = mockk()
        mockUserPreferencesMapper = mockk()
        mockStoreRepository = mockk()

        repository = ProfileRepositoryImpl(
            mockApiService,
            mockUserPreferences,
            mockUserPreferencesMapper,
            mockStoreRepository
        )
    }

//    @Test
//    fun `fetchCurrentUser should return user domain model`() = runTest {
//        // Given
//        val mockUser = mockk<UserRemoteModel>()
//        val mockDomainUser = mockk<UserDomainModel>()
//        val mockStores = listOf(mockk<StoreUserDomainModel>())
//
//        coEvery { mockApiService.getCurrentUser() } returns mockk {
//            every { data } returns mockUser
//        }
//        every { mockUser.toDomainUser() } returns mockDomainUser
//        every { mockDomainUser.stores } returns mockStores
//        coEvery { mockStoreRepository.saveStores(mockStores) } just Runs
//        coEvery { repository.saveCurrentUser(mockDomainUser) } just Runs
//
//        // When
//        val result = repository.fetchCurrentUser()
//
//        // Then
//        assertNotNull(result)
//        assertEquals(mockDomainUser, result)
//    }

    @Test
    fun `fetchUserList should return paging data flow`() = runTest {
        // Given
        val mockPagingSource = mockk<UserPagingSource>()
        val mockPagingData = mockk<PagingData<UserDomainModel>>()

        coEvery { mockApiService.getUsers(any()) } returns mockk {
            every { data } returns listOf(mockk())
        }

        // When
        val result = repository.fetchUserList()

        // Then
        assertNotNull(result)
        result.first() // Verify flow can be collected
    }

    @Test
    fun `fetchStoreUserList should return list of users`() = runTest {
        // Given
        val mockUsers = listOf(
            UserRemoteModel(id = "user1", firstName = "John", lastName = "Doe"),
            UserRemoteModel(id = "user2", firstName = "Jane", lastName = "Smith")
        )
        coEvery { mockApiService.getStoreUsers() } returns mockk {
            every { data } returns mockUsers
        }

        // When
        val result = repository.fetchStoreUserList()

        // Then
        assertNotNull(result)
        assertEquals(mockUsers.size, result.size)
    }

    @Test
    fun `createUser should return user domain model`() = runTest {
        // Given
        val mockUser = UserRemoteModel(id="id1", firstName = "John", lastName = "Doe",email = "john@gmail.com")
        val mockDomainUser = UserDomainModel(id="id1", firstName = "John", lastName = "Doe", fullName = "John Doe", email = "john@gmail.com")
        val request = hashMapOf<String, Any>()

        coEvery { mockApiService.createUser(request) } returns mockk {
            every { data } returns mockUser
        }

        // When
        val result = repository.createUser(request)

        // Then
        assertNotNull(result)
        assertEquals(mockDomainUser, result)
    }

    @Test
    fun `activate should return activated user domain model`() = runTest {
        // Given
        val userId = "123"
        val mockUser = UserRemoteModel(id="123", firstName = "John", lastName = "Doe",email = "john@gmail.com", status = 1)
        val mockDomainUser = UserDomainModel(id="123", firstName = "John", lastName = "Doe", fullName = "John Doe", email = "john@gmail.com", status = 1)

        coEvery { mockApiService.activateUser(userId) } returns mockk {
            every { data } returns mockUser
        }

        // When
        val result = repository.activate(userId)

        // Then
        assertNotNull(result)
        assertEquals(mockDomainUser, result)
    }

    @Test
    fun `deactivate should return deactivated user domain model`() = runTest {
        // Given
        val userId = "123"
        val mockUser = UserRemoteModel(id="123", firstName = "John", lastName = "Doe",email = "john@gmail.com", status = 2)
        val mockDomainUser = UserDomainModel(id="123", firstName = "John", lastName = "Doe", fullName = "John Doe", email = "john@gmail.com", status = 2)

        coEvery { mockApiService.deactivateUser(userId) } returns mockk {
            every { data } returns mockUser
        }

        // When
        val result = repository.deactivate(userId)

        // Then
        assertNotNull(result)
        assertEquals(mockDomainUser, result)
    }

    @Test
    fun `getCurrentUser should return user domain model flow`() = runTest {
        // Given
        val mockUser = mockk<UserDomainModel>()
        val mockPreferences = mockk<UserPreferences>()

        every { mockUserPreferences.data } returns flowOf(mockPreferences)
        every { mockUserPreferencesMapper.toDomain(mockPreferences) } returns mockUser

        // When
        val result = repository.getCurrentUser()

        // Then
        assertNotNull(result)
        assertEquals(mockUser, result.first())
    }

    @Test
    fun `saveCurrentUser should update user preferences`() = runTest {
        // Given
        val mockUser = mockk<UserDomainModel>()
        val mockPreferences = mockk<UserPreferences>()
        val mockUpdatedPreferences = mockk<UserPreferences>()

        coEvery { mockUserPreferences.updateData(any()) } returns mockUpdatedPreferences
        every { mockUser.email } returns "test@example.com"
        every { mockUser.id } returns "123"
        every { mockUser.status } returns 1
        every { mockUser.phone } returns "1234567890"
        every { mockUser.firstName } returns "John"
        every { mockUser.lastName } returns "Doe"
        every { mockUser.role } returns 1

        // When
        repository.saveCurrentUser(mockUser)

        // Then
        coVerify { mockUserPreferences.updateData(any()) }
    }

    @Test
    fun `assignUserToStore should return success message`() = runTest {
        // Given
        val storeId = "store123"
        val request = hashMapOf<String, Any>()

        coEvery { mockApiService.assignUserToStore(storeId, request) } returns GenericUserResponse(data = Any())

        // When
        val result = repository.assignUserToStore(storeId, request)

        // Then
        assertEquals("Success", result)
    }

    @Test
    fun `fetchUnassignedUsers should return list of users`() = runTest {
        // Given
        val mockUsers = listOf(
            UserRemoteModel(id = "user1", firstName = "John", lastName = "Doe"),
            UserRemoteModel(id = "user2", firstName = "Jane", lastName = "Smith")
        )
        val request = hashMapOf<String, Any>()

        coEvery { mockApiService.getUnassignedUsers(request) } returns mockk {
            every { data } returns mockUsers
        }

        // When
        val result = repository.fetchUnassignedUsers(request)

        // Then
        assertNotNull(result)
        assertEquals(mockUsers.size, result.size)
    }
} 