package com.dennytech.data.impl

import com.dennytech.data.local.dao.ProductCategoryDao
import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.dao.StoreUserDao
import com.dennytech.data.local.mappers.ProductCategoryEntityMapper
import com.dennytech.data.local.mappers.StoreEntityMapper
import com.dennytech.data.local.mappers.StoreUserEntityMapper
import com.dennytech.data.local.models.ProductCategoryEntity
import com.dennytech.data.local.models.StoreEntity
import com.dennytech.data.local.models.StoreUserEntity
import com.dennytech.data.remote.models.RemoteStoreModel
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ProductCategoryDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.StoreUserDomainModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StoreRepositoryImplTest {
    private lateinit var repository: StoreRepositoryImpl
    private lateinit var apiService: ApiService
    private lateinit var storeDao: StoreDao
    private lateinit var storeEntityMapper: StoreEntityMapper
    private lateinit var storeUserDao: StoreUserDao
    private lateinit var storeUserEntityMapper: StoreUserEntityMapper
    private lateinit var productCategoryDao: ProductCategoryDao
    private lateinit var categoryEntityMapper: ProductCategoryEntityMapper

    @Before
    fun setup() {
        apiService = mockk()
        storeDao = mockk()
        storeEntityMapper = mockk()
        storeUserDao = mockk()
        storeUserEntityMapper = mockk()
        productCategoryDao = mockk()
        categoryEntityMapper = mockk()

        repository = StoreRepositoryImpl(
            apiService,
            storeDao,
            storeEntityMapper,
            storeUserDao,
            storeUserEntityMapper,
            productCategoryDao,
            categoryEntityMapper
        )
    }

    @Test
    fun `createStore should return domain model when API call succeeds`() = runTest {
        // Given
        val request = hashMapOf<String, Any>()
        val remoteStore = RemoteStoreModel(id="123")
        
        coEvery { apiService.createStore(request) } returns mockk {
            every { data } returns remoteStore
        }
//        every { remoteStore.toDomain("") } returns domainStore

        // When
        val result = repository.createStore(request)

        // Then
        assertEquals(remoteStore.id, result.id)
        coVerify { apiService.createStore(request) }
    }

    @Test(expected = Exception::class)
    fun `getStoreById should throw exception when store not found`() = runTest {
        // Given
        val storeId = "nonExistentStoreId"
        coEvery { storeDao.getById(storeId) } returns emptyList()

        // When
        repository.getStoreById(storeId)
    }

    @Test
    fun `saveStore should save store, users and categories`() = runTest {
        // Given
        val store = mockk<StoreDomainModel>()
        val storeEntity = mockk<StoreEntity>()
        val storeUserEntity = mockk<StoreUserEntity>()
        val users = listOf(mockk<StoreUserDomainModel>())
        val categories = listOf(mockk<ProductCategoryDomainModel>())


        coEvery { storeDao.insert(any()) } returns 1L
        coEvery { storeUserDao.insert(any()) } returns 1L
        coEvery { productCategoryDao.insert(any()) } returns 1L
        every { storeEntityMapper.toLocal(any()) } returns storeEntity
        every { storeUserEntityMapper.toLocal(any()) } returns storeUserEntity
        every { categoryEntityMapper.toLocal(any()) } returns mockk<ProductCategoryEntity>()
        every { store.users } returns users
        every { store.categories } returns categories

        // When
        repository.saveStore(store)

        // Then
        coVerify { storeDao.insert(storeEntity) }
        coVerify { storeUserDao.insert(any()) }
        coVerify { productCategoryDao.insert(any()) }
    }

    @Test
    fun `saveProductCategory should save category`() = runTest {
        // Given
        val category = mockk<ProductCategoryDomainModel>()
        val categoryEntity = mockk<ProductCategoryEntity>()
        coEvery { productCategoryDao.insert(any()) } returns 1L
        every { categoryEntityMapper.toLocal(category) } returns categoryEntity

        // When
        repository.saveProductCategory(category)

        // Then
        coVerify { productCategoryDao.insert(categoryEntity) }
    }

    @Test
    fun `saveStoreUser should save store user`() = runTest {
        // Given
        val storeUser = mockk<StoreUserDomainModel>()
        val storeUserEntity = mockk<StoreUserEntity>()
        coEvery { storeUserDao.insert(any()) } returns 1L
        every { storeUserEntityMapper.toLocal(storeUser) } returns storeUserEntity

        // When
        repository.saveStoreUser(storeUser)

        // Then
        coVerify { storeUserDao.insert(storeUserEntity) }
    }

    @Test
    fun `saveStores should clear and save all stores`() = runTest {
        // Given
        val stores = listOf(StoreDomainModel(id ="123"))
        val storeUserEntity = mockk<StoreUserEntity>()
        val storeEntity = mockk<StoreEntity>()
        coEvery { storeDao.clear() } just Runs
        every { storeUserDao.clear() } just Runs
        every { productCategoryDao.clear() } just Runs
        coEvery { storeDao.insert(any()) } returns 1L
        every { storeUserEntityMapper.toLocal(any()) } returns storeUserEntity
        every { storeEntityMapper.toLocal(any()) } returns storeEntity

        // When
        repository.saveStores(stores)

        // Then
        coVerify { storeDao.clear() }
        verify { storeUserDao.clear() }
        verify { productCategoryDao.clear() }
        coVerify { storeDao.insert(any()) }
    }
} 