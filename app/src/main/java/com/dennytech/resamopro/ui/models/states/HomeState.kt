package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.resamopro.ui.models.CountCardModel

data class HomeState(
    val revenue: Int = 0,
    val counts: List<CountCardModel> = emptyList(),
    val sales: List<SaleDomainModel> = emptyList(),
    val userStores: List<StoreDomainModel> = emptyList(),
    val loadingCounts: Boolean = false,
    val loadingRevenue: Boolean = false,
    val loadingSales: Boolean = false,
    val currentStore: StoreDomainModel? = null,
    val showStoreBottomSheet: Boolean = false,
    val loadingSaleByPeriod: Boolean = false,
    val salePeriodReport: List<SaleReportDomainModel> = emptyList(),
)
