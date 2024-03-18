package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteProductModel.Companion.toDomain
import com.dennytech.domain.models.SaleDomainModel

data class RemoteSaleListResponse(
    val data: List<RemoteSaleModel>
)

data class ConfirmSaleResponse(
    val statusCode: Int
)

data class RemoteSaleModel(
    val id: String,
    val product: RemoteProductModel?,
    val profit: Int?,
    val sellingPrice: Int?,
    val quantity: Int?,
    val saleDate: String?,
    val collected: Boolean?
) {
    companion object {
        fun RemoteSaleModel.toDomain() : SaleDomainModel {
            return SaleDomainModel(
                id = this.id,
                product = this.product?.toDomain(),
                profit = this.profit ?: 0,
                sellingPrice = this.sellingPrice ?: 0,
                quantity = this.quantity ?: 0,
                saleDate = this.saleDate.orEmpty(),
                collected = this.collected ?: false
            )
        }
    }
}
