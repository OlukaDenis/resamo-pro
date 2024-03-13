package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.ProfileEntity
import com.dennytech.domain.models.ProfileDomainModel
import javax.inject.Inject

class ProfileEntityMapper @Inject constructor(): BaseLocalMapper<ProfileEntity, ProfileDomainModel> {
    override fun toDomain(entity: ProfileEntity): ProfileDomainModel {
        return ProfileDomainModel(
            id = entity.id,
            userId = entity.userId,
            address = null,
            linked = null,
            mobileCountryCode = entity.mobileCountryCode,
            mobileNumber = entity.mobileNumber,
            name = entity.name,
            role = entity.role,
            paymentMethods = emptyList(),
            trulipayUser = entity.trulipayUser
        )
    }

    override fun toLocal(entity: ProfileDomainModel): ProfileEntity {
        return ProfileEntity(
            id = entity.id,
            userId = entity.userId,
            mobileCountryCode = entity.mobileCountryCode,
            mobileNumber = entity.mobileNumber,
            name = entity.name,
            role = entity.role,
            trulipayUser = entity.trulipayUser
        )
    }
}