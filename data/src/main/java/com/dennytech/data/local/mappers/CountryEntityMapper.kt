package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.CountryEntity
import com.dennytech.domain.models.CountryDomainModel
import javax.inject.Inject

class CountryEntityMapper @Inject constructor(): BaseLocalMapper<CountryEntity, CountryDomainModel> {
    override fun toDomain(entity: CountryEntity): CountryDomainModel {
        return CountryDomainModel(
            name = entity.name,
            countryCode = entity.countryCode,
            currency = entity.currency,
            dialCode = entity.dialCode
        )
    }

    override fun toLocal(entity: CountryDomainModel): CountryEntity {
        return CountryEntity(
            id = 0L,
            name = entity.name,
            countryCode = entity.countryCode,
            currency = entity.currency,
            dialCode = entity.dialCode
        )
    }
}