package com.dennytech.domain.repository

import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {
    suspend fun fetchSalesByPeriod(): List<SaleReportDomainModel>
    fun getSalesReport(): Flow<List<SaleReportDomainModel>>
    suspend fun fetchPopularProductTypes(): List<ReportDomainModel>
    suspend fun saveSaleReportToCache(reports: List<SaleReportDomainModel>)
    suspend fun fetchCounts(request: HashMap<String, Any>): InsightCountsDomainModel
    fun getInsights(): Flow<InsightCountsDomainModel?>
    suspend fun saveInsightsToCache(insight: InsightCountsDomainModel)
}