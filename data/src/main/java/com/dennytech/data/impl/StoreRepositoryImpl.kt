package com.dennytech.data.impl

import com.dennytech.data.local.dao.ProductCategoryDao
import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.dao.StoreUserDao
import com.dennytech.data.local.mappers.ProductCategoryEntityMapper
import com.dennytech.data.local.mappers.StoreEntityMapper
import com.dennytech.data.local.mappers.StoreUserEntityMapper
import com.dennytech.data.remote.models.RemoteStoreModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ProductCategoryDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.StoreUserDomainModel
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val storeDao: StoreDao,
    private val storeEntityMapper: StoreEntityMapper,
    private val storeUserDao: StoreUserDao,
    private val storeUserEntityMapper: StoreUserEntityMapper,
    private val productCategoryDao: ProductCategoryDao,
    private val categoryEntityMapper: ProductCategoryEntityMapper
) : StoreRepository {
    override suspend fun createStore(request: HashMap<String, Any>): StoreDomainModel {
        return try {
            val response = apiService.createStore(request).data
            response.toDomain("")
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getUserStores(): Flow<List<StoreDomainModel>> = flow {
        storeDao.get().map {list ->
            emit(list.map { entity ->
                val store = storeEntityMapper.toDomain(entity)
                val usersList = storeUserDao.getStoreUsers(entity.id).first()  // Pick store users
                val categoryList =  productCategoryDao.getStoreCategories(entity.id).first()  // Pick store categories
                store.copy(
                    users = usersList.map { storeUserEntityMapper.toDomain(it) },
                    categories = categoryList.map { categoryEntityMapper.toDomain(it) }
                )
            })
        }.first()
    }

    override suspend fun getStoreById(storeId: String): StoreDomainModel {
        val stores = storeDao.getById(storeId).map { storeEntityMapper.toDomain(it) }

        if (stores.isEmpty()) throw Exception("store not found")

        val store = stores[0]
        val usersList = storeUserDao.getStoreUsers(store.id).first()  // Pick store users
        val categoryList = productCategoryDao.getStoreCategories(store.id).first()  // Pick store categories
        return store.copy(
            users = usersList.map { storeUserEntityMapper.toDomain(it) },
            categories = categoryList.map { categoryEntityMapper.toDomain(it) }
        )

    }

    override suspend fun saveStore(store: StoreDomainModel) {
        storeDao.insert(storeEntityMapper.toLocal(store))

        if (store.users.isNotEmpty()) {
            store.users.map {
                 saveStoreUser(it)
            }
        }

        if (store.categories.isNotEmpty()) {
            store.categories.map {
                saveProductCategory(it)
            }
        }
    }

    override suspend fun saveProductCategory(category: ProductCategoryDomainModel) {
        productCategoryDao.insert(categoryEntityMapper.toLocal(category))
    }

    override suspend fun saveStoreUser(storeUser: StoreUserDomainModel) {
        storeUserDao.insert(storeUserEntityMapper.toLocal(storeUser))
    }

    override suspend fun saveStores(stores: List<StoreDomainModel>) {
        storeDao.clear()
       storeUserDao.clear()
       productCategoryDao.clear()

        stores.map {
            saveStore(it)
        }
    }
}