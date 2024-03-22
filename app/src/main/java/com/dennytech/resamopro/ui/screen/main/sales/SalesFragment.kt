package com.dennytech.resamopro.ui.screen.main.sales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dennytech.domain.models.SaleDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.DateFilter
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.SaleItem
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesFragment(
    viewModel: SalesViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.sales),
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
    ) {values ->

        Column(
            modifier = Modifier.padding(values)
        ) {

            Column(
                modifier = Modifier.padding(Dimens._0dp)
            ) {
                val cellCount = 2
                val products: LazyPagingItems<SaleDomainModel> =
                    viewModel.salesState.collectAsLazyPagingItems()

                Column(
                    modifier = Modifier
                        .padding(Dimens._16dp),
                ) {

                    DateFilter(
                        startDate = viewModel.state.startDate,
                        endDate = viewModel.state.endDate,
                        endDateChange = {viewModel.onEvent(SaleEvent.EndDateChanged(it))},
                        startDateChange = {viewModel.onEvent(SaleEvent.StartDateChanged(it))},
                        onSubmit = {viewModel.onEvent(SaleEvent.FilterSales)}
                    )
                    VerticalSpacer(Dimens._16dp)

                    Text(
                        text = "Sales",
                        textAlign = TextAlign.Center,
                        fontSize = Dimens._16sp,
                        color = DeepSeaBlue
                    )
                }

                if (viewModel.state.loading) {
                    LoadingCircle()
                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(horizontal = Dimens._16dp, vertical = Dimens._14dp),
                    columns = StaggeredGridCells.Fixed(cellCount),
                    horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
                    verticalItemSpacing = Dimens._14dp
                ) {
                    items(
                        products.itemCount,
                    ) { index ->
                        val item = products[index]!!
                        SaleItem(
                            sale = item,
                            onClick = {
//                                viewModel.onEvent(SaleEvent.ConfirmSale(item.id))
                            },
                            onItemSelected = {
                                Timber.d("Item: %s", it)
                                viewModel.onEvent(SaleEvent.ConfirmSale(it))
                            }
                        )
                    }

                    products.apply {
                        when {

                            loadState.refresh is LoadState.Loading -> {
                                viewModel.state = viewModel.state.copy(loading = true)
                            }

                            loadState.source.refresh is LoadState.NotLoading -> {
                                viewModel.state = viewModel.state.copy(loading = false)
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = products.loadState.refresh as LoadState.Error
                                item {
                                    ErrorLabel(message = error.error.localizedMessage ?: "Error")
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                Timber.d("Next page item...")
                            }

                            loadState.append is LoadState.Error -> {
                                val error = products.loadState.append as LoadState.Error
                                item {
                                    ErrorLabel(message = error.error.localizedMessage ?: "Error")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}