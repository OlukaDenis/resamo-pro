package com.dennytech.resamopro.ui.screen.main.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.DateFilter
import com.dennytech.resamopro.ui.components.InsightCard
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.SaleItem
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.components.home.CurrentStoreInfo
import com.dennytech.resamopro.ui.components.home.HomeUserInfo
import com.dennytech.resamopro.ui.components.home.NotActivatedCard
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.RedLight400
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.TruliRed
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

            LaunchedEffect(Unit) {
//                viewModel.initialize()
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
                                onClick = {},
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

        val counts = viewModel.state.counts

        mainViewModel.state.user?.let {
            if (it.role == 1) {

                InsightCard(
                    title = "Total Revenue",
                    value = viewModel.state.revenue.toDouble().formatCurrency(),
                    valueTextSize = Dimens._28sp,
                    backgroundColor = DeepSeaBlue,
                    foregroundColor = Color.White
                )
                VerticalSpacer(Dimens._16dp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Monthly Report",
                textAlign = TextAlign.Center,
                fontSize = Dimens._16sp,
                color = DeepSeaBlue
            )

            TextButton(
                onClick = { navigateToCounts() },
            ) {
                Text("More...")
            }
        }

        if (viewModel.state.loadingCounts) {
            LoadingCircle()
        }

        CountsCards(counts = counts)

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Sales",
                textAlign = TextAlign.Center,
                fontSize = Dimens._16sp,
                color = DeepSeaBlue
            )

            TextButton(
                onClick = { navigateToSales() },
                modifier = Modifier.padding(Dimens._12dp),
            ) {
                Text("See All")
            }
        }

        VerticalSpacer(Dimens._10dp)

        if (viewModel.state.loadingSales) {
            LoadingCircle()
        }

        RecentSales(mainViewModel = mainViewModel, viewModel = viewModel)
    }
}


@Composable
fun CountsCards(
    counts: List<CountCardModel>,
) {

    if (counts.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = 1000.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
        ) {
            items(counts) { item ->
                InsightCard(title = item.title, value = item.content)
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