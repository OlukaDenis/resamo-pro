package com.dennytech.resamopro.ui.screen.main.sales

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.SaleItem
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
//                actions = {
//                    mainViewModel.state.user?.let {
//                        if(it.role == 1) {
//                            IconButton(onClick = { navigateToNewUser() }) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Add,
//                                    contentDescription = stringResource(id = R.string.add),
//                                    tint = Color.Black
//                                )
//                            }
//                        }
//                    }
//
//                }
            )
        }
    ) {values ->

        if (viewModel.state.loading) {
            Toast.makeText(
                context,
                "Please wait...",
                Toast.LENGTH_SHORT
            ).show()
        }

        Column(
            modifier = Modifier.padding(values)
        ) {

            Column(
                modifier = Modifier.padding(Dimens._0dp)
            ) {
                val cellCount = 2

                val products: LazyPagingItems<SaleDomainModel> =
                    viewModel.salesState.collectAsLazyPagingItems()
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
                            onClick = {}
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
}