package com.dennytech.data.impl

import androidx.datastore.core.DataStore
import com.dennytech.data.InsightCounts
import com.dennytech.data.local.dao.SaleReportDao
import com.dennytech.data.local.mappers.InsightCountMapper
import com.dennytech.data.local.mappers.SaleReportEntityMapper
import com.dennytech.data.remote.models.RemoteReportModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleCountsModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteSaleReportModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.InsightCountsDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.repository.ReportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val salesReportDao: SaleReportDao,
    private val saleReportEntityMapper: SaleReportEntityMapper,
    private val insightCountPreference: DataStore<InsightCounts>,
    private val insightCountMapper: InsightCountMapper
) : ReportsRepository {
    override suspend fun fetchSalesByPeriod(): List<SaleReportDomainModel> {
        return try {
            val response = apiService.getSalesByPeriod()
            response.data.map { it.toDomain() }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override suspend fun saveSaleReportToCache(reports: List<SaleReportDomainModel>) {
        try {
            salesReportDao.clear()
            reports.forEach {
                salesReportDao.insert(saleReportEntityMapper.toLocal(it))
            }
        } catch (e: Exception) {
            Timber.e("Failed to save sale reports to cache: ", e)
        }
    }

    override suspend fun saveInsightsToCache(insight: InsightCountsDomainModel) {
        insightCountPreference.updateData { pref ->
            pref.toBuilder()
                .setSalesCount(insight.salesCount)
                .setSalesTotal(insight.salesTotal)
                .setProfit(insight.profit)
                .build()
        }
    }

    override fun getInsights(): Flow<InsightCountsDomainModel?> {
        return try {
            insightCountPreference.data.map { 
                if (it == InsightCounts.getDefaultInstance()) {
                    null
                } else {
                    insightCountMapper.toDomain(it)
                }
            }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    override fun getSalesReport(): Flow<List<SaleReportDomainModel>> {
        return salesReportDao.get().map { list ->
            list.map { saleReportEntityMapper.toDomain(it) }
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

    override suspend fun fetchCounts(request: HashMap<String, Any>): InsightCountsDomainModel {
        return try {
            val response = apiService.fetchSaleCounts(request)
            response.data.toDomain()
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}