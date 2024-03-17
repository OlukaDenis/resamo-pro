package com.dennytech.resamopro.ui.screen.main.home

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
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.SaleItem
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.utils.Helpers.formatCurrency

@Composable
fun HomeFragment(
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSales: () -> Unit
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
                viewModel.initialize()
            }

            Column(
                modifier = Modifier
                    .padding(Dimens._16dp),
            ) {

                VerticalSpacer(Dimens._16dp)

                Text(
                    text = "Hello, ${mainViewModel.state.user?.firstName}",
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._24sp,
                    color = DeepSeaBlue
                )

                VerticalSpacer(Dimens._16dp)

                mainViewModel.state.user?.let {
                    if (it.role == 1) {
                        RevenueCard(mainViewModel = mainViewModel, viewModel = viewModel)
                        VerticalSpacer(Dimens._16dp)
                    }
                }

                CountsCards(mainViewModel = mainViewModel, viewModel = viewModel)

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
                RecentSales(mainViewModel = mainViewModel, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun RevenueCard(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens._0dp
        ),
        shape = RoundedCornerShape(Dimens._8dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens._18dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Column {
                Text(
                    text = "Total Revenue",
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._10sp,
                )
                VerticalSpacer(Dimens._4dp)

                Text(
                    text = viewModel.state.revenue.toDouble().formatCurrency(),
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._28sp,
                    color = DeepSeaBlue,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
private fun CountsCards(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel,
) {
    val counts =  viewModel.state.counts

    if (counts.isNotEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier.heightIn(max = 1000.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
        ) {
            items(counts) { item ->
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimens._0dp
                    ),
                    shape = RoundedCornerShape(Dimens._8dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(Dimens._18dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {


                        Column {
                            Text(
                                text = item.title,
                                textAlign = TextAlign.Center,
                                fontSize = Dimens._10sp,
                            )
                            VerticalSpacer(Dimens._4dp)

                            Text(
                                text = item.content,
                                textAlign = TextAlign.Center,
                                fontSize = Dimens._16sp,
                                color = DeepSeaBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
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

    if (list.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
        ) {
            items(list) { item ->
                SaleItem(
                    sale = item,
                    onClick = {}
                )
            }
        }
    }

}