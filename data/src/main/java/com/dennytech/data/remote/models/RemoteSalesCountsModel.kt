package com.dennytech.data.remote.models

import com.dennytech.domain.models.SaleCountsDomainModel

data class RemoteSaleCountsResponse (
    val data: RemoteSaleCountsModel
)

data class RemoteSaleCountsModel(
    val profit: Int?,
    val salesCount: Int?,
    val salesTotal: Int?
) {
    companion object {
        fun RemoteSaleCountsModel.toDomain() : SaleCountsDomainModel {
            return SaleCountsDomainModel(
                profit = this.profit ?: 0,
                salesCount = this.salesCount ?: 0,
                salesTotal = this.salesTotal ?: 0
            )
        }
    }
}