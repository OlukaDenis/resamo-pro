package com.dennytech.data.impl

import com.dennytech.data.local.dao.CountryDao
import com.dennytech.data.local.dao.ProvinceDao
import com.dennytech.data.local.dao.StateDao
import com.dennytech.data.local.mappers.CountryEntityMapper
import com.dennytech.data.local.mappers.ProvinceEntityMapper
import com.dennytech.data.local.mappers.StateEntityMapper
import com.dennytech.domain.models.CountryDomainModel
import com.dennytech.domain.models.ProvinceDomainModel
import com.dennytech.domain.models.StateDomainModel
import com.dennytech.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val stateDao: StateDao,
    private val provinceDao: ProvinceDao,
    private val countryDao: CountryDao,
    private val stateEntityMapper: StateEntityMapper,
    private val provinceEntityMapper: ProvinceEntityMapper,
    private val countryEntityMapper: CountryEntityMapper
) : LocationRepository {
    override fun getCountries(): Flow<List<CountryDomainModel>> {
        return countryDao.get().map { list -> list.map { countryEntityMapper.toDomain(it) } }
    }

    override fun getStates(): Flow<List<StateDomainModel>> {
        return stateDao.get().map { list -> list.map { stateEntityMapper.toDomain(it) } }
    }

    override fun getProvinces(): Flow<List<ProvinceDomainModel>> {
        return provinceDao.get().map { list -> list.map { provinceEntityMapper.toDomain(it) } }
    }
}