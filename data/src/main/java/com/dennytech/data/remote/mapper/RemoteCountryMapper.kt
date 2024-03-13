package com.dennytech.data.remote.mapper

import com.dennytech.data.remote.models.CountryRemoteModel
import com.dennytech.domain.models.CountryDomainModel
import javax.inject.Inject

class RemoteCountryMapper @Inject constructor(): BaseRemoteMapper<CountryRemoteModel, CountryDomainModel> {
    override fun toDomain(entity: CountryRemoteModel): CountryDomainModel {
        return  CountryDomainModel(
            name = entity.display.orEmpty(),
            countryCode = entity.ISO2.orEmpty(),
            dialCode = entity.dialCode.orEmpty(),
            currency = entity.currency?.get(0).orEmpty()
        )
    }
}