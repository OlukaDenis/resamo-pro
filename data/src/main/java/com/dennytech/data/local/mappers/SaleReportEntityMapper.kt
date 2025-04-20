package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.SaleReportEntity
import com.dennytech.domain.models.SaleReportDomainModel
import javax.inject.Inject

class SaleReportEntityMapper @Inject constructor(): BaseLocalMapper<SaleReportEntity, SaleReportDomainModel> {
    override fun toDomain(entity: SaleReportEntity): SaleReportDomainModel {
        return SaleReportDomainModel(
            count = entity.count,
            monthYear = entity.monthYear,
            period = entity.period,
            revenue = entity.revenue,
            total = entity.total
        )
    }

    override fun toLocal(entity: SaleReportDomainModel): SaleReportEntity {
        return SaleReportEntity(
            count = entity.count,
            monthYear = entity.monthYear,
            period = entity.period,
            revenue = entity.revenue,
            total = entity.total
        )
    }

}