package com.dennytech.domain.models

data class SaleReportDomainModel(
    val count: Int,
    val monthYear: String,
    val period: String,
    val revenue: Int,
    val total: Int
)

data class ReportDomainModel(
    val count: Int,
    val revenue: Int,
    val type: String,
    val amount: Int
)