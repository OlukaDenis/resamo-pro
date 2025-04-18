package com.dennytech.domain.models

data class SaleReportDomainModel(
    val count: Int = -1,
    val monthYear: String = "",
    val period: String = "",
    val revenue: Int = -1,
    val total: Int = -1
)

data class ReportDomainModel(
    val count: Int,
    val revenue: Int,
    val type: String,
    val amount: Int
)