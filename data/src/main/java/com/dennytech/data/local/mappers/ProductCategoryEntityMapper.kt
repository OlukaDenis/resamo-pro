package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.ProductCategoryEntity
import com.dennytech.domain.models.ProductCategoryDomainModel
import javax.inject.Inject

class ProductCategoryEntityMapper @Inject constructor(): BaseLocalMapper<ProductCategoryEntity, ProductCategoryDomainModel> {
    override fun toDomain(entity: ProductCategoryEntity): ProductCategoryDomainModel {
        return ProductCategoryDomainModel(
            id = entity.id,
            storeId = entity.storeId,
            name = entity.name
        )
    }

    override fun toLocal(entity: ProductCategoryDomainModel): ProductCategoryEntity {
        return ProductCategoryEntity(
            id = entity.id,
            storeId = entity.storeId,
            name = entity.name
        )
    }
}