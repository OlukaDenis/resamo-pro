package com.dennytech.domain.repository

import com.dennytech.domain.models.ProductCategoryDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.StoreUserDomainModel
import com.dennytech.domain.models.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    suspend fun createStore(request: HashMap<String, Any>): StoreDomainModel
    fun getUserStores(): Flow<List<StoreDomainModel>>
    suspend fun getStoreById(storeId: String): StoreDomainModel
    suspend fun saveStore(store: StoreDomainModel)
    suspend fun saveStoreUser(storeUser: StoreUserDomainModel)
    suspend fun saveProductCategory(category: ProductCategoryDomainModel)
    suspend fun saveStores(stores: List<StoreDomainModel>)
}