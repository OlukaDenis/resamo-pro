package com.dennytech.data.remote.models

import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.SaleReportDomainModel

data class SaleReportModelResponse(
    val data: List<RemoteSaleReportModel>
)

data class ReportModelResponse(
    val data: List<RemoteReportModel>
)

data class RemoteSaleReportModel(
    val count: Int?,
    val monthYear: String?,
    val period: String?,
    val revenue: Int?,
    val total: Int?
) {

    companion object {
        fun RemoteSaleReportModel.toDomain(): SaleReportDomainModel {
            return SaleReportDomainModel(
                count = this.count ?: 0,
                monthYear = this.monthYear.orEmpty(),
                period = this.period.orEmpty(),
                revenue = this.revenue ?: 0,
                total = this.total ?: 0
            )
        }
    }
}

data class RemoteReportModel(
    val count: Int?,
    val revenue: Int?,
    val type: String?,
    val amount: Int?
) {
    companion object {
        fun RemoteReportModel.toDomain(): ReportDomainModel {
            return ReportDomainModel(
                count = this.count ?: 0,
                amount = this.amount ?: 0,
                type = this.type.orEmpty(),
                revenue = this.revenue ?: 0,
            )
        }
    }
}