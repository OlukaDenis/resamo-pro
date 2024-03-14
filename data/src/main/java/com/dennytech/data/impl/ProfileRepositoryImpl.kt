package com.dennytech.data.impl

import com.dennytech.data.local.dao.AddressDao
import com.dennytech.data.local.dao.PaymentMethodDao
import com.dennytech.data.local.dao.ProfileDao
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.mappers.AddressEntityMapper
import com.dennytech.data.local.mappers.PaymentMethodEntityMapper
import com.dennytech.data.local.mappers.ProfileEntityMapper
import com.dennytech.data.local.mappers.UserEntityMapper
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.UserDomainModel
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
    private val userEntityMapper: UserEntityMapper,
    private val apiService: ApiService
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

    override fun getCurrentUser(): Flow<UserDomainModel?> = flow {
        userDao.getTopUser().map {
            if (it.isEmpty()) emit(null) else emit(userEntityMapper.toDomain(it[0]))
        }.first()
    }
}