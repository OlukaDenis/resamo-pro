package com.dennytech.resamopro.ui.screen.main.home.components.saleReport

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.SaleReportDomainModel
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.charts.CombinedBarGraph

@Composable
fun SaleReportComponent(
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

            val dataOne = reports.map { it.total.toFloat() }
            val dataTwo = reports.map { it.revenue.toFloat() }
            val xData = reports.indices.map { it.toFloat() }
            val xLabels = reports.map { it.period }.toTypedArray()

            CombinedBarGraph(
                xData = xData,
                firstValues = dataOne,
                secondValues = dataTwo,
                xLabels = xLabels,
                legendEnabled = true,
                graphOneLabel = "Sales",
                graphTwoLabel = "Revenue"
            )
        }
        is Resource.Error ->  {
            // TODO implement a retry operation
        }
    }

}