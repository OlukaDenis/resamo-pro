package com.dennytech.data.impl

import com.dennytech.data.remote.models.RemoteReportModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleReportModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.repository.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): ReportsRepository {
    override suspend fun fetchSalesByPeriod(): List<SaleReportDomainModel> {
        return try {
            val response = apiService.getSalesByPeriod()
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun fetchPopularProductTypes(): List<ReportDomainModel> {
        return try {
            val response = apiService.getPopularProductTypes()
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}