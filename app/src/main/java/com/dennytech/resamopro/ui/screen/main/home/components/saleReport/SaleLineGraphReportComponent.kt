package com.dennytech.resamopro.ui.screen.main.home.components.saleReport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.charts.FilledLineGraph

@Composable
fun SaleLineGraphReportComponent(
    viewModel: SaleReportViewModel = hiltViewModel(),
) {

    LaunchedEffect(true) {
        viewModel.fetchAndObserveReport()
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is Resource.Loading ->  LoadingCircle()
        is Resource.Success ->  {
            val reports = (uiState as Resource.Success<List<SaleReportDomainModel>>).data

            val yData = reports.map { it.total.toFloat() }
            val xData = reports.indices.map { it.toFloat() }
            val xLabels = reports.map { it.period }.toTypedArray()

            FilledLineGraph(
                xData = xData,
                yData = yData,
                xLabels = xLabels
            )
        }
        is Resource.Error ->  {
            // TODO implement a retry operation
        }
    }

}