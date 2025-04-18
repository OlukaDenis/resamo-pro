package com.dennytech.resamopro.ui.screen.main.home.components.insightCounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.Resource
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.screen.main.home.CountCardModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliOrange

@Composable
fun InsightReportComponent(
    viewModel: InsightReportViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.fetchAndObserveInsights()
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is Resource.Loading -> LoadingCircle()
        is Resource.Success -> {
            val insights = (uiState as Resource.Success<List<CountCardModel>>).data
            InsightCardGrid(insights = insights)
        }

        is Resource.Error -> {
            // TODO implement a retry operation
        }
    }
}

@Composable
fun InsightCardGrid(insights: List<CountCardModel>) {
    VerticalSpacer(Dimens._8dp)
    LazyVerticalGrid(
        modifier = Modifier.heightIn(max = 1000.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
    ) {
        items(insights) { item ->
            val index = insights.indexOf(item)
            com.dennytech.resamopro.ui.components.InsightCard(
                title = item.title,
                value = item.content,
                backgroundColor = if (index == 0) TruliBlue else TruliOrange,
                foregroundColor = Color.White
            )
        }
    }
}