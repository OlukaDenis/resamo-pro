package com.dennytech.domain.repository

import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.SaleReportDomainModel

interface ReportsRepository {
    suspend fun fetchSalesByPeriod(): List<SaleReportDomainModel>
    suspend fun fetchPopularProductTypes(): List<ReportDomainModel>
}