package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.StoreEntity
import com.dennytech.domain.models.StoreDomainModel
import javax.inject.Inject

class StoreEntityMapper @Inject constructor() : BaseLocalMapper<StoreEntity, StoreDomainModel> {
    override fun toDomain(entity: StoreEntity): StoreDomainModel {
        return StoreDomainModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            status = entity.status,
            location = entity.location,
            userId = entity.userId,
            createdBy = "",
            users = emptyList(),
            categories = emptyList(),
            brands = entity.brands,
            productTypes = entity.productTypes
        )
    }

    override fun toLocal(entity: StoreDomainModel): StoreEntity {
        return StoreEntity(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            status = entity.status,
            location = entity.location,
            userId = entity.userId,
            brands = entity.brands,
            productTypes = entity.productTypes
        )
    }

}