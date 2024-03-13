package com.dennytech.data.remote.mapper

import com.dennytech.data.remote.models.ProvinceRemoteModel
import com.dennytech.domain.models.ProvinceDomainModel
import javax.inject.Inject

class RemoteProvinceMapper @Inject constructor(): BaseRemoteMapper<ProvinceRemoteModel, ProvinceDomainModel> {
    override fun toDomain(entity: ProvinceRemoteModel): ProvinceDomainModel {
        return ProvinceDomainModel(
            name = entity.display.orEmpty(),
            abbreviation = entity.abbrv.orEmpty()
        )
    }
}