package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.EmptyComponent
import com.dennytech.resamopro.ui.components.InsightCard
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.SaleItem
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.components.charts.CombinedBarGraph
import com.dennytech.resamopro.ui.components.home.CurrentStoreInfo
import com.dennytech.resamopro.ui.components.home.HomeUserInfo
import com.dennytech.resamopro.ui.components.home.NotActivatedCard
import com.dennytech.resamopro.ui.components.home.StoresBottomSheet
import com.dennytech.resamopro.ui.screen.main.home.components.insightCounts.InsightReportComponent
import com.dennytech.resamopro.ui.screen.main.home.components.revenue.RevenueComponent
import com.dennytech.resamopro.ui.screen.main.home.components.saleReport.SaleReportComponent
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.ui.theme.TruliOrange
import com.dennytech.resamopro.utils.Helpers.formatCurrency

@Composable
fun HomeFragment(
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSales: () -> Unit,
    navigateToCounts: () -> Unit
) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            LaunchedEffect(true) {
                viewModel.initialize()
            }

            Column(
                modifier = Modifier
                    .padding(Dimens._16dp),
            ) {

                VerticalSpacer(Dimens._16dp)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeUserInfo()

                    mainViewModel.state.user?.let {
                        if (it.isAdmin()) {
                            CurrentStoreInfo(
                                onClick = { viewModel.onEvent(HomeEvent.ToggleStoreBottomSheet) },
                                storeName = viewModel.state.currentStore?.name ?: ""
                            )
                        }
                    }
                }

                VerticalSpacer(Dimens._16dp)

                mainViewModel.state.user?.let {
                    if (it.status == 1) {
                        HomeContent(
                            navigateToCounts = navigateToCounts,
                            navigateToSales = navigateToSales
                        )
                    }

                    if (it.status == 2) {
                        VerticalSpacer(Dimens._16dp)
                        NotActivatedCard()
                    }
                }

                if (viewModel.state.showStoreBottomSheet) {
                    StoresBottomSheet(
                        onDismiss = { viewModel.onEvent(HomeEvent.ToggleStoreBottomSheet) }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSales: () -> Unit,
    navigateToCounts: () -> Unit
) {
    Column {

        mainViewModel.state.user?.let {
            if (it.isAdmin()) {
                RevenueComponent()
            }
        }

        InsightsDivider(
            leftText = "Reports",
            rightText = "More...",
            onNavigateClick = navigateToCounts)
        InsightReportComponent()

        mainViewModel.state.user?.let {
            if (it.isAdmin()) {
                VerticalSpacer(Dimens._16dp)
//                SaleRevenueGraph(viewModel = viewModel)
                SaleReportComponent()
            }
        }

        InsightsDivider(
            leftText = "Recent Sales",
            rightText = "See All",
            onNavigateClick = navigateToCounts)

        if (viewModel.state.loadingSales) {
            LoadingCircle()
        }

        RecentSales(mainViewModel = mainViewModel, viewModel = viewModel)
    }
}


@Composable
private fun InsightsDivider(
    leftText: String,
    rightText: String,
    onNavigateClick: (() -> Unit)? = null,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = leftText,
                textAlign = TextAlign.Center,
                fontSize = Dimens._16sp,
                color = DeepSeaBlue
            )

            if (onNavigateClick != null) {
                TextButton(
                    onClick = { onNavigateClick() },
                ) {
                    Text(rightText)
                }
            }
        }
    }
}

@Composable
private fun RecentSales(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel,
) {
    val list = viewModel.state.sales

    if (!viewModel.state.loadingSales && list.isEmpty()) {
        EmptyComponent()
    }

    if (list.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
        ) {
            items(list) { item ->
                SaleItem(
                    sale = item,
                    onClick = {},
                    onItemSelected = {}
                )
            }
        }
    }

}