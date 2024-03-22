package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.mappers.UserEntityMapper
import com.dennytech.data.local.mappers.UserPreferencesMapper
import com.dennytech.data.remote.datasource.ProductPagingSource
import com.dennytech.data.remote.datasource.UserPagingSource
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.remote.services.AuthService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val preferenceRepository: PreferenceRepository,
    private val userEntityMapper: UserEntityMapper,
    private val apiService: ApiService,
    private val userPreferences: DataStore<UserPreferences>,
    private val userPreferencesMapper: UserPreferencesMapper
) : ProfileRepository {
    override suspend fun fetchUser(request: HashMap<String, Any>): UserDomainModel {
        return try {
            val response = runBlocking { apiService.getCurrentUser() }
            val user = response.data.toDomain()

            // Clear users
            userDao.clear()

            runBlocking { userDao.insert(userEntityMapper.toLocal(user)) }

            user
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchUserList(): Flow<PagingData<UserDomainModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                UserPagingSource(apiService)
            }
        ).flow
    }

    override suspend fun createUser(request: HashMap<String, Any>): UserDomainModel {
        return try {
            apiService.createUser(request).data.toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun activate(userId: String): UserDomainModel {
        return try {
            apiService.activateUser(userId).data.toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun deactivate(userId: String): UserDomainModel {
        return try {
           apiService.deactivateUser(userId).data.toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getCurrentUser(): Flow<UserDomainModel?> {
        return try {
            userPreferences.data.map { userPreferencesMapper.toDomain(it) }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}