package com.dennytech.data.remote.models

import com.dennytech.domain.models.InsightCountsDomainModel

data class RemoteSaleCountsResponse (
    val data: RemoteSaleCountsModel
)

data class RemoteSaleCountsModel(
    val profit: Long?,
    val salesCount: Long?,
    val salesTotal: Long?
) {
    companion object {
        fun RemoteSaleCountsModel.toDomain() : InsightCountsDomainModel {
            return InsightCountsDomainModel(
                profit = this.profit ?: 0L,
                salesCount = this.salesCount ?: 0L,
                salesTotal = this.salesTotal ?: 0L
            )
        }
    }
}