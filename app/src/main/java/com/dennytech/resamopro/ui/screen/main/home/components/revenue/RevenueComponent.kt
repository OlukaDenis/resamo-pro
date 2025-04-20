package com.dennytech.resamopro.ui.screen.main.home.components.revenue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.Resource
import com.dennytech.resamopro.ui.components.InsightCard
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun RevenueComponent(
    viewModel: RevenueViewModel = hiltViewModel()
) {

    LaunchedEffect(true) {
        viewModel.fetchAndObserveRevenue()
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is Resource.Loading ->  LoadingCircle()
        is Resource.Success ->  {
            val revenue = (uiState as Resource.Success<String>).data
            InsightCard(
                title = "Total Revenue",
                value = revenue,
                valueTextSize = Dimens._28sp,
                backgroundColor = DeepSeaBlue,
                foregroundColor = Color.White
            )
        }
        is Resource.Error ->  {
            // TODO implement a retry operation
        }
    }

}