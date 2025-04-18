package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.SaleReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleReportDao: BaseDao<SaleReportEntity> {
    @Query("SELECT * FROM sale_report")
    fun get(): Flow<List<SaleReportEntity>>

    @Query("SELECT * FROM sale_report WHERE period = :period")
    suspend fun getByPeriod(period: String): List<SaleReportEntity>

    @Query("DELETE FROM sale_report")
    suspend fun clear()
}