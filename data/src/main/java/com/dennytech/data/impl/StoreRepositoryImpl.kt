package com.dennytech.data.impl

import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.dao.StoreUserDao
import com.dennytech.data.local.mappers.StoreEntityMapper
import com.dennytech.data.local.mappers.StoreUserEntityMapper
import com.dennytech.data.remote.models.RemoteStoreModel.Companion.toDomain
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomainUser
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.StoreUserDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val storeDao: StoreDao,
    private val storeEntityMapper: StoreEntityMapper,
    private val storeUserDao: StoreUserDao,
    private val storeUserEntityMapper: StoreUserEntityMapper,
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
            emit(list.map { entity ->
                val store = storeEntityMapper.toDomain(entity)
                val usersList = runBlocking { storeUserDao.getStoreUsers(entity.id).first() } // Pick store users
                store.copy(users = usersList.map { storeUserEntityMapper.toDomain(it) })
            })
        }.first()
    }

    override suspend fun getStoreById(storeId: String): StoreDomainModel {
        val stores = storeDao.getById(storeId).map { storeEntityMapper.toDomain(it) }

        if (stores.isEmpty()) throw Exception("store not found")

        val store = stores[0]
        val usersList = runBlocking { storeUserDao.getStoreUsers(store.id).first() } // Pick store users
        return store.copy(users = usersList.map { storeUserEntityMapper.toDomain(it) })
    }

    override suspend fun saveStore(store: StoreDomainModel) {
        storeDao.insert(storeEntityMapper.toLocal(store))

        if (store.users.isNotEmpty()) {
            store.users.map {
                runBlocking { saveStoreUser(it) }
            }
        }
    }

    override suspend fun saveStoreUser(storeUser: StoreUserDomainModel) {
        storeUserDao.insert(storeUserEntityMapper.toLocal(storeUser))
    }

    override suspend fun saveStores(stores: List<StoreDomainModel>) {
        runBlocking { storeDao.clear() }

        stores.map {
            saveStore(it)
        }
    }
}