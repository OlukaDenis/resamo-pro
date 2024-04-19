package com.dennytech.data.impl

import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.mappers.StoreEntityMapper
import com.dennytech.data.remote.models.RemoteStoreModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val storeDao: StoreDao,
    private val storeEntityMapper: StoreEntityMapper,
) : StoreRepository {
    override suspend fun createStore(request: HashMap<String, Any>): StoreDomainModel {
        return try {
            val response = runBlocking { apiService.createStore(request)}.data
            response.toDomain(response.createdBy.orEmpty())
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getUserStores(): Flow<List<StoreDomainModel>> = flow {
        storeDao.get().map {list ->
            emit(list.map { storeEntityMapper.toDomain(it) })
        }
    }

    override suspend fun getStoreById(storeId: String): List<StoreDomainModel> {
        return storeDao.getById(storeId).map { storeEntityMapper.toDomain(it) }
    }

    override suspend fun saveStore(store: StoreDomainModel) {
        storeDao.insert(storeEntityMapper.toLocal(store))
    }

}