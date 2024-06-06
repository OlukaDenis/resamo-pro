package com.dennytech.resamopro.ui.screen.main.home.counts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.DateFilter
import com.dennytech.resamopro.ui.components.InsightCard
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.components.charts.BarGraph
import com.dennytech.resamopro.ui.components.charts.CombinedBarGraph
import com.dennytech.resamopro.ui.components.charts.FilledLineGraph
import com.dennytech.resamopro.ui.components.charts.PieGraph
import com.dennytech.resamopro.ui.screen.main.home.CountsCards
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.utils.Helpers.capitalize
import com.dennytech.resamopro.utils.Helpers.formatCurrency
import com.github.mikephil.charting.data.PieEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountsFragment(
    viewModel: CountsViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.sales_report),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.Black
                        )
                    }
                },
            )
        }
    ) { values ->

        Box(
            modifier = Modifier
                .padding(values)
        ) {

            val counts = viewModel.state.counts

            LaunchedEffect(key1 = true) {
                mainViewModel.state.user?.let {
                    if (it.isAdmin()) {
                        viewModel.getAdminReports()
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(Dimens._16dp)
                    .verticalScroll(rememberScrollState()),
            ) {

                VerticalSpacer(Dimens._16dp)

                DateFilter(
                    startDate = viewModel.state.startDate,
                    endDate = viewModel.state.endDate,
                    endDateChange = { viewModel.onEvent(CountsEvent.EndDateChanged(it)) },
                    startDateChange = { viewModel.onEvent(CountsEvent.StartDateChanged(it)) },
                    onSubmit = { viewModel.onEvent(CountsEvent.SubmitFilter) }
                )

                VerticalSpacer(Dimens._16dp)

                CountsCards(
                    counts = counts,
                    loading = viewModel.state.loadingCounts
                )

                mainViewModel.state.user?.let {
                    if (it.isAdmin()) {
                        VerticalSpacer(Dimens._16dp)

                        InsightCard(
                            title = "Generated Revenue",
                            value = viewModel.state.revenue.toDouble().formatCurrency()
                        )

                        AdminReports(viewModel = viewModel)
                    }
                }
            }
        }

    }
}

@Composable
fun AdminReports(viewModel: CountsViewModel) {
    Column {
//        VerticalSpacer(Dimens._24dp)
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Monthly Sales and Revenue",
//                textAlign = TextAlign.Center,
//                fontSize = Dimens._18sp,
//                color = DeepSeaBlue
//            )
//        }
//        VerticalSpacer(Dimens._10dp)
//        SalesAndRevenueGraph(viewModel = viewModel)


        VerticalSpacer(Dimens._24dp)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sales by Month",
                textAlign = TextAlign.Center,
                fontSize = Dimens._18sp,
                color = DeepSeaBlue
            )
        }
        VerticalSpacer(Dimens._10dp)
        SalesByPeriodLineGraph(viewModel = viewModel)



//        VerticalSpacer(Dimens._24dp)
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Revenue by Month",
//                textAlign = TextAlign.Center,
//                fontSize = Dimens._18sp,
//                color = DeepSeaBlue
//            )
//        }
//        VerticalSpacer(Dimens._10dp)
//        SalesByPeriodBarGraph(viewModel = viewModel)

        VerticalSpacer(Dimens._24dp)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Product Types",
                textAlign = TextAlign.Center,
                fontSize = Dimens._18sp,
                color = DeepSeaBlue
            )
        }
        VerticalSpacer(Dimens._10dp)
        PopularTypesPieGraph(viewModel = viewModel)
    }
}

@Composable
private fun SalesAndRevenueGraph(viewModel: CountsViewModel) {
    if (!viewModel.state.loadingSaleByPeriod) {
        val reports = viewModel.state.salePeriodReport
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
}

@Composable
private fun SalesByPeriodLineGraph(viewModel: CountsViewModel) {
    if (!viewModel.state.loadingSaleByPeriod) {
        val reports = viewModel.state.salePeriodReport
        val yData = reports.map { it.total.toFloat() }
        val xData = reports.indices.map { it.toFloat() }
        val xLabels = reports.map { it.period }.toTypedArray()

        FilledLineGraph(
            xData = xData,
            yData = yData,
            xLabels = xLabels
        )
    } else {
        LoadingCircle()
    }
}

@Composable
private fun SalesByPeriodBarGraph(viewModel: CountsViewModel) {
    if (!viewModel.state.loadingSaleByPeriod) {
        val reports = viewModel.state.salePeriodReport
        val yData = reports.map { it.revenue.toFloat() }
        val xData = reports.indices.map { it.toFloat() }
        val xLabels = reports.map { it.period }.toTypedArray()

        BarGraph(
            xData = xData,
            yData = yData,
            xLabels = xLabels
        )
    }
}


@Composable
private fun PopularTypesPieGraph(viewModel: CountsViewModel) {
    if (!viewModel.state.loadingPopularTypes) {
        val reports = viewModel.state.popularTypes
        val values = reports.map {
            PieEntry(it.count.toFloat(), "${it.type.capitalize()}(${it.count})")
        }

        PieGraph(
            entries = values
        )
    } else {
        LoadingCircle()
    }
}