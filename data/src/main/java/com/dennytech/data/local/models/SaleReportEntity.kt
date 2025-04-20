package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_report")
data class SaleReportEntity(
    val count: Int,
    val monthYear: String,
    @PrimaryKey
    val period: String,
    val revenue: Int,
    val total: Int
)