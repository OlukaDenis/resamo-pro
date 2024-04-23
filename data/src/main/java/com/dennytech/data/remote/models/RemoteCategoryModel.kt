package com.dennytech.data.remote.models

import com.dennytech.domain.models.ProductCategoryDomainModel

data class RemoteCategoryModel(
    val id: String,
    val name: String?
) {
    companion object {
        fun RemoteCategoryModel.toDomain(storeId: String): ProductCategoryDomainModel {
            return ProductCategoryDomainModel(
                id = this.id,
                storeId = storeId,
                name = this.name.orEmpty()
            )
        }
    }
}