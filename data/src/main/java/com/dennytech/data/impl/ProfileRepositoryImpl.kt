package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.UserPreferences
import com.dennytech.data.local.dao.AddressDao
import com.dennytech.data.local.dao.PaymentMethodDao
import com.dennytech.data.local.dao.ProfileDao
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.mappers.AddressEntityMapper
import com.dennytech.data.local.mappers.PaymentMethodEntityMapper
import com.dennytech.data.local.mappers.ProfileEntityMapper
import com.dennytech.data.local.mappers.UserEntityMapper
import com.dennytech.data.local.mappers.UserPreferencesMapper
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao,
    private val profileDao: ProfileDao,
    private val userDao: UserDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val profileEntityMapper: ProfileEntityMapper,
    private val addressEntityMapper: AddressEntityMapper,
    private val paymentMethodEntityMapper: PaymentMethodEntityMapper,
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
            profileDao.clear()
            paymentMethodDao.clear()
            addressDao.clear()

            runBlocking { userDao.insert(userEntityMapper.toLocal(user)) }

            user
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

    override suspend fun refreshToken(): String {
        return try {

            val response = apiService.refreshToken()
            val data  = response.data;

            runBlocking { preferenceRepository.setAccessToken(data.refreshToken.orEmpty()) }
            runBlocking { preferenceRepository.setTokenExpiry(data.expiresIn ?: 0L) }

            data.refreshToken.orEmpty()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}