package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.mappers.UserPreferencesMapper
import com.dennytech.data.remote.datasource.UserPagingSource
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomainUser
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.ProfileRepository
import com.dennytech.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: DataStore<UserPreferences>,
    private val userPreferencesMapper: UserPreferencesMapper,
    private val storeRepository: StoreRepository
) : ProfileRepository {
    override suspend fun fetchCurrentUser(): UserDomainModel {
        return try {
            val response = apiService.getCurrentUser()
            val user = response.data.toDomainUser()

            saveCurrentUser(user) 

            if (user.stores.isNotEmpty()) {
                storeRepository.saveStores(user.stores)
            }

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

    override suspend fun fetchStoreUserList(): List<UserDomainModel> {
        return try {
            val users = apiService.getStoreUsers().data
            users.map { it.toDomainUser() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun createUser(request: HashMap<String, Any>): UserDomainModel {
        return try {
            apiService.createUser(request).data.toDomainUser()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun activate(userId: String): UserDomainModel {
        return try {
            apiService.activateUser(userId).data.toDomainUser()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun deactivate(userId: String): UserDomainModel {
        return try {
           apiService.deactivateUser(userId).data.toDomainUser()
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

    override suspend fun saveCurrentUser(user: UserDomainModel) {
        userPreferences.updateData { prefs ->
            prefs.toBuilder()
                .setEmail(user.email)
                .setId(user.id)
                .setStatus(user.status)
                .setPhone(user.phone)
                .setFirstName(user.firstName)
                .setLastName(user.lastName)
                .setRole(user.role)
                .build()
        }
    }

    override suspend fun assignUserToStore(storeId: String, request: HashMap<String, Any>) : String{
        return try {
            apiService.assignUserToStore(storeId, request)
            "Success"
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchUnassignedUsers(request: HashMap<String, Any>): List<UserDomainModel> {
        return try {
            val users = apiService.getUnassignedUsers(request).data
            users.map { it.toDomainUser() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}