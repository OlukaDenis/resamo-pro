package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.AddressEntity
import com.dennytech.domain.models.AddressDomainModel
import javax.inject.Inject

class AddressEntityMapper @Inject constructor(): BaseLocalMapper<AddressEntity, AddressDomainModel> {
    override fun toDomain(entity: AddressEntity): AddressDomainModel {
        return AddressDomainModel(
            id = entity.id,
            profileId = entity.profileId,
            userId = entity.userId,
            address = entity.address,
            address2 = entity.address2,
            city = entity.city,
            postalCode = entity.postalCode,
            countryCode = entity.countryCode,
            province = entity.province
        )
    }

    override fun toLocal(entity: AddressDomainModel): AddressEntity {
        return AddressEntity(
            id = entity.id,
            profileId = entity.profileId,
            userId = entity.userId,
            address = entity.address,
            address2 = entity.address2,
            city = entity.city,
            postalCode = entity.postalCode,
            countryCode = entity.countryCode,
            province = entity.province,
        )
    }
}