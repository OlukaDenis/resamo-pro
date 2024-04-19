package com.dennytech.domain.repository

import com.dennytech.domain.models.StoreDomainModel
import kotlinx.coroutines.flow.Flow

interface StoreRepository {
    suspend fun createStore(request: HashMap<String, Any>): StoreDomainModel
    fun getUserStores(): Flow<List<StoreDomainModel>>
    suspend fun getStoreById(storeId: String): List<StoreDomainModel>
    suspend fun saveStore(store: StoreDomainModel)
}