package com.dennytech.data.local.mappers

import com.dennytech.data.InsightCounts
import com.dennytech.domain.models.InsightCountsDomainModel
import javax.inject.Inject

class InsightCountMapper @Inject constructor() : BaseLocalMapper<InsightCounts, InsightCountsDomainModel> {
    override fun toDomain(entity: InsightCounts): InsightCountsDomainModel {
        return InsightCountsDomainModel(
            salesTotal = entity.salesTotal,
            salesCount = entity.salesCount,
            profit = entity.profit
        )
    }

    override fun toLocal(entity: InsightCountsDomainModel): InsightCounts {
        return InsightCounts.getDefaultInstance()
    }

}