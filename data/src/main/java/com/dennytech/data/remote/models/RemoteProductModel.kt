package com.dennytech.data.remote.models

import com.dennytech.domain.models.ProductDomainModel

data class ProductListResponseModel(
    val data: List<RemoteProductModel>
)


data class ProductResponseModel(
    val data: RemoteProductModel
)


data class RemoteProductModel(
    val brand: String?,
    val categoryId: String?,
    val color: String?,
    val createdAt: String?,
    val createdBy: String?,
    val id: String?,
    val image: String?,
    val thumbnail: String?,
    val inStock: Boolean?,
    val name: String?,
    val price: Int?,
    val quantity: Int?,
    val size: Int?,
    val type: String?,
    val updatedAt: String?
) {

    companion object {
        fun RemoteProductModel.toDomain(): ProductDomainModel {
            return ProductDomainModel(
                brand = this.brand.orEmpty(),
                categoryId = this.categoryId.orEmpty(),
                color = this.color.orEmpty(),
                createdAt = this.createdAt.orEmpty(),
                createdBy = this.createdBy.orEmpty(),
                id = this.id.orEmpty(),
                image = this.image.orEmpty(),
                inStock = this.inStock ?: false,
                name =  this.name.orEmpty(),
                price = this.price ?: 0,
                quantity = this.quantity ?: 0,
                size = this.size ?: 0,
                thumbnail = this.thumbnail.orEmpty(),
                updatedAt = this.updatedAt.orEmpty(),
                type = this.type.orEmpty(),
            )
        }
    }
}