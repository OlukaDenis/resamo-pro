package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.StoreUserEntity
import com.dennytech.domain.models.StoreUserDomainModel
import javax.inject.Inject

class StoreUserEntityMapper @Inject constructor() : BaseLocalMapper<StoreUserEntity, StoreUserDomainModel> {
    override fun toDomain(entity: StoreUserEntity): StoreUserDomainModel {
        return StoreUserDomainModel(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            email = entity.email,
            role = entity.role,
            lastLogin = entity.lastLogin,
            status = entity.status,
            fullName = "${entity.firstName} ${entity.lastName}",
            stores = emptyList(),
            defaultStore = "",
            storeId = entity.storeId
        )
    }

    override fun toLocal(entity: StoreUserDomainModel): StoreUserEntity {
        return StoreUserEntity(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            phone = entity.phone,
            email = entity.email,
            role = entity.role,
            lastLogin = entity.lastLogin,
            status = entity.status,
            fullName = "${entity.firstName} ${entity.lastName}",
            storeId = entity.storeId
        )
    }
}