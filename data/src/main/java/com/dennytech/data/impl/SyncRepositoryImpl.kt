package com.dennytech.data.impl

import com.dennytech.data.local.dao.CountryDao
import com.dennytech.data.local.dao.ProvinceDao
import com.dennytech.data.local.dao.StateDao
import com.dennytech.data.local.mappers.CountryEntityMapper
import com.dennytech.data.local.mappers.ProvinceEntityMapper
import com.dennytech.data.local.mappers.StateEntityMapper
import com.dennytech.data.remote.mapper.RemoteCountryMapper
import com.dennytech.data.remote.mapper.RemoteProvinceMapper
import com.dennytech.data.remote.mapper.StateRemoteMapper
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.CountryDomainModel
import com.dennytech.domain.models.ProvinceDomainModel
import com.dennytech.domain.models.StateDomainModel
import com.dennytech.domain.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val stateDao: StateDao,
    private val provinceDao: ProvinceDao,
    private val countryDao: CountryDao,
    private val remoteStateMapper: StateRemoteMapper,
    private val remoteProvinceMapper: RemoteProvinceMapper,
    private val remoteCountryMapper: RemoteCountryMapper,
    private val stateEntityMapper: StateEntityMapper,
    private val provinceEntityMapper: ProvinceEntityMapper,
    private val countryEntityMapper: CountryEntityMapper
): SyncRepository {
    override suspend fun fetchStates(): List<StateDomainModel> {
        return try {

            val response = service.getStateList()
            val list = response.data.map { remoteStateMapper.toDomain(it) }
            if (list.isNotEmpty()) stateDao.clear()
            list.map { stateEntityMapper.toLocal(it) }.map {
                stateDao.insert(it)
            }

            list
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchProvinces(): List<ProvinceDomainModel> {
        return try {

            val response = service.getProvinceList()
            val list = response.data.map { remoteProvinceMapper.toDomain(it) }
            if (list.isNotEmpty()) provinceDao.clear()
            list.map { provinceEntityMapper.toLocal(it) }.map {
                provinceDao.insert(it)
            }

            list
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchCountries(): List<CountryDomainModel> {
        return try {

            val response = service.getCountryList()
            val list = response.data.map { remoteCountryMapper.toDomain(it) }
            if (list.isNotEmpty()) countryDao.clear()
            list.map { countryEntityMapper.toLocal(it) }.map {
                countryDao.insert(it)
            }

            list
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}