package com.dennytech.data.remote.models

import com.dennytech.domain.models.ProductDomainModel

data class ProductListResponseModel(
    val data: List<RemoteProductModel>
)


data class ProductResponseModel(
    val data: RemoteProductModel
)

data class CreateSaleResponseModel(
    val statusCode: Int
)


data class RemoteProductModel(
    val brand: String?= null,
    val categoryId: String?= null,
    val color: String?= null,
    val createdAt: String?= null,
    val createdBy: String?= null,
    val id: String?= null,
    val image: String?= null,
    val thumbnail: String?= null,
    val inStock: Boolean?= null,
    val name: String?= null,
    val price: Int?= null,
    val quantity: Int?= null,
    val size: Int?= null,
    val type: String?= null,
    val updatedAt: String?= null,
    val damaged: Boolean?= null,
    val volume: Int?= null,
    val volumeUnit: String?= null,
    val weight: Int?= null,
    val weightUnit: String?= null
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
                price = this.price ?: -1,
                quantity = this.quantity ?: -1,
                size = this.size ?: -1,
                thumbnail = this.thumbnail.orEmpty(),
                updatedAt = this.updatedAt.orEmpty(),
                type = this.type.orEmpty(),
                damaged = this.damaged ?: false,
                volume = this.volume ?: -1,
                volumeUnit = this.volumeUnit.orEmpty(),
                weight = this.weight ?: -1,
                weightUnit = this.weightUnit.orEmpty()
            )
        }
    }
}