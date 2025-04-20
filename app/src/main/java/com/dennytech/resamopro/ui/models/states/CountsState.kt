package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.ReportDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.resamopro.ui.models.CountCardModel

data class CountsState(
    val counts: List<CountCardModel> = emptyList(),
    val loadingCounts: Boolean = false,
    val revenue: Long = 0,
    val endDate: String = "",
    val startDate: String = "",
    val loadingSaleByPeriod: Boolean = false,
    val salePeriodReport: List<SaleReportDomainModel> = emptyList(),
    val loadingPopularTypes: Boolean = false,
    val popularTypes: List<ReportDomainModel> = emptyList()
)
