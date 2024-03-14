package com.dennytech.resamopro.ui.screen.main.products

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.models.ProductFilerModel.Companion.isNoEmpty
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.FilterDialog
import com.dennytech.resamopro.ui.components.ProductItem
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.google.gson.GsonBuilder
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsFragment(
    viewModel: ProductViewModel = hiltViewModel(),
    navController: NavController,
    navigateUp: () -> Unit,
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
                        text = stringResource(R.string.products),
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
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ProductEvent.ToggleFilterDialog)}) {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = stringResource(id = R.string.filter),
                            tint = if(viewModel.state.filters.isNoEmpty()) TruliBlue else Color.Black
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            viewModel.state.showFilterDialog -> {
                FilterDialog(
                    dismissDialog = { viewModel.onEvent(ProductEvent.ToggleFilterDialog)},
                    confirm = {
                        viewModel.onEvent(ProductEvent.ToggleFilterDialog)
                        viewModel.onEvent(ProductEvent.GetProducts)
                    }

                )
            }
        }

        Column(
            modifier = Modifier.padding(padding)
        ) {
            val cellCount = 2

            val products: LazyPagingItems<ProductDomainModel> =
                viewModel.productsState.collectAsLazyPagingItems()
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(horizontal = Dimens._16dp, vertical = Dimens._14dp),
                columns = GridCells.Fixed(cellCount),
                horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
                verticalArrangement = Arrangement.spacedBy(Dimens._12dp),
//                contentPadding = PaddingValues(bottom = Dimens._30dp),
            ) {
                items(
                    products.itemCount,
                ) { index ->
                    val item = products[index]!!
                    ProductItem(
                        product = item,
                        onClick = {
//                            val gson = GsonBuilder().create()
//                            val userJson = gson.toJson(item)
//
//                            navController.navigate(
//                                "detail/{transaction}"
//                                    .replace(
//                                        oldValue = "{transaction}",
//                                        newValue = userJson
//                                    )
//                            )
                        }
                    )
                }

                products.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(top = Dimens._16dp)
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = Dimens._2dp,
                                            modifier = Modifier
                                                .height(Dimens._24dp)
                                                .width(Dimens._24dp)
                                        )
                                    }
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = products.loadState.refresh as LoadState.Error
                            item {
                                ErrorLabel(message = error.error.localizedMessage!!)
//                        ErrorMessage(
//                            modifier = Modifier.fillParentMaxSize(),
//                            message = error.error.localizedMessage!!,
//                            onClickRetry = { retry() })
                            }
                        }

                        loadState.append is LoadState.Loading -> {
//                    item { LoadingNextPageItem(modifier = Modifier) }
                            Timber.d("Next page item...")
                        }

                        loadState.append is LoadState.Error -> {
                            val error = products.loadState.append as LoadState.Error
                            item {
                                ErrorLabel(message = error.error.localizedMessage!!)

//                        ErrorMessage(
//                            modifier = Modifier,
//                            message = error.error.localizedMessage!!,
//                            onClickRetry = { retry() })
                            }
                        }
                    }
                }
            }
        }
    }
}