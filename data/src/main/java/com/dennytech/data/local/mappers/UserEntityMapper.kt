package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.UserEntity
import com.dennytech.domain.models.UserDomainModel
import javax.inject.Inject

class UserEntityMapper @Inject constructor() : BaseLocalMapper<UserEntity, UserDomainModel> {
    override fun toDomain(entity: UserEntity): UserDomainModel {
        return UserDomainModel(
            id = entity.id,
            name = entity.name,
            username = entity.username,
            email = entity.email,
            mobileCountryCode = entity.mobileCountryCode,
            mobileNumber = entity.mobileNumber,
            faceIdEnabled = entity.faceIdEnabled,
            address = null,
            dob = entity.dob,
            accountBalance = entity.accountBalance,
            pinSetup = entity.pinSetup,
            currency = entity.currency,
            kycStatus = entity.kycStatus,
            profileImage = entity.profileImage,
            receivers = emptyList(),
            senders = emptyList(),
            paymentMethods = emptyList()
        )
    }

    override fun toLocal(entity: UserDomainModel): UserEntity {
        return UserEntity(
            id = entity.id,
            name = entity.name,
            username = entity.username,
            email = entity.email,
            mobileCountryCode = entity.mobileCountryCode,
            mobileNumber = entity.mobileNumber,
            faceIdEnabled = entity.faceIdEnabled,
            dob = entity.dob,
            accountBalance = entity.accountBalance,
            pinSetup = entity.pinSetup,
            currency = entity.currency,
            kycStatus = entity.kycStatus,
            profileImage = entity.profileImage,
        )
    }
}