package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.UserEntity
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.domain.models.UserDomainModel
import javax.inject.Inject

class UserEntityMapper @Inject constructor() : BaseLocalMapper<UserEntity, UserDomainModel> {
    override fun toDomain(entity: UserEntity): UserDomainModel {
        return UserDomainModel(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            email = entity.email,
            role = entity.role,
            lastLogin = entity.lastLogin,
            status = entity.status,
            fullName = "${entity.firstName} ${entity.lastName}"
        )
    }

    override fun toLocal(entity: UserDomainModel): UserEntity {
        return UserEntity(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            email = entity.email,
            role = entity.role,
            lastLogin = entity.lastLogin,
            status = entity.status,
            fullName = "${entity.firstName} ${entity.lastName}"
        )
    }
}