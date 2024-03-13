package com.dennytech.domain.repository

import com.dennytech.domain.models.CountryDomainModel
import com.dennytech.domain.models.ProvinceDomainModel
import com.dennytech.domain.models.StateDomainModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCountries(): Flow<List<CountryDomainModel>>
    fun getStates(): Flow<List<StateDomainModel>>
    fun getProvinces(): Flow<List<ProvinceDomainModel>>
}