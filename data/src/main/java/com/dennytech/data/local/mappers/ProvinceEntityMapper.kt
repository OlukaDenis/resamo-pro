package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.ProvinceEntity
import com.dennytech.domain.models.ProvinceDomainModel
import javax.inject.Inject

class ProvinceEntityMapper @Inject constructor(): BaseLocalMapper<ProvinceEntity, ProvinceDomainModel> {
    override fun toDomain(entity: ProvinceEntity): ProvinceDomainModel {
        return ProvinceDomainModel(name = entity.name, abbreviation = entity.abbreviation)
    }

    override fun toLocal(entity: ProvinceDomainModel): ProvinceEntity {
        return ProvinceEntity(id = 0L, name = entity.name, abbreviation = entity.abbreviation)
    }

}