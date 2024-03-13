package com.dennytech.domain.repository

import com.dennytech.domain.models.CountryDomainModel
import com.dennytech.domain.models.ProvinceDomainModel
import com.dennytech.domain.models.StateDomainModel


interface SyncRepository {
    suspend fun fetchStates(): List<StateDomainModel>
    suspend fun fetchProvinces(): List<ProvinceDomainModel>
    suspend fun fetchCountries(): List<CountryDomainModel>
}