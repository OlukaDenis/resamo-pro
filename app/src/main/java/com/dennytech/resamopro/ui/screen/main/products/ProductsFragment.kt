package com.dennytech.resamopro.ui.screen.main.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.models.ProductFilerModel.Companion.isNoEmpty
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.FilterDialog
import com.dennytech.resamopro.ui.components.ProductItem
import com.dennytech.resamopro.ui.navigation.MainScreen
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey100
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsFragment(
    viewModel: ProductViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToNewProduct: () -> Unit,
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
                actions = {

                    mainViewModel.state.user?.let {
                        if(it.role == 1) {
                            IconButton(onClick = { navigateToNewProduct() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = stringResource(id = R.string.filter),
                                    tint = Color.Black
                                )
                            }
                        }
                    }

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

            viewModel.state.showPreviewDialog -> {
                PreviewImage()
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
        ) {
            val cellCount = 2

            val products: LazyPagingItems<ProductDomainModel> =
                viewModel.productsState.collectAsLazyPagingItems()
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(horizontal = Dimens._16dp, vertical = Dimens._14dp),
                columns = StaggeredGridCells.Fixed(cellCount),
                horizontalArrangement = Arrangement.spacedBy(Dimens._16dp),
                verticalItemSpacing = Dimens._14dp
//                verticalArrangement = Arrangement.spacedBy(Dimens._12dp),
//                contentPadding = PaddingValues(bottom = Dimens._30dp),
            ) {
                items(
                    products.itemCount,
                ) { index ->
                    val item = products[index]!!
                    ProductItem(
                        product = item,
                        preview = {
                            viewModel.onEvent(ProductEvent.SelectImage(image = item.image))
                            viewModel.onEvent(ProductEvent.TogglePreviewDialog)
                        },
                        onClick = {
                            val gson = GsonBuilder().create()
                            val objectJson = gson.toJson(item)
                            val encode = URLEncoder.encode(objectJson, StandardCharsets.UTF_8.toString())

                            mainViewModel.state.user?.let {
                                if (it.role == 1) {
                                    navController.navigate(
                                        MainScreen.UpdateProduct.route
                                            .replace(
                                                oldValue = "{product}",
                                                newValue = encode
                                            )
                                    )
//                                    Timber.d("Product on click: %s", objectJson)
                                } else {
                                    navController.navigate(
                                        MainScreen.ProductDetail.route
                                            .replace(
                                                oldValue = "{product}",
                                                newValue = encode
                                            )
                                    )
                                }
                            }

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

@Composable
fun PreviewImage(
    viewModel: ProductViewModel = hiltViewModel(),
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { viewModel.onEvent(ProductEvent.TogglePreviewDialog) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens._14dp),
            colors = CardDefaults.cardColors(
                containerColor = Grey100,
            ),
            shape = RoundedCornerShape(Dimens._8dp),
        ) {

           Column {

               SubcomposeAsyncImage(
                   model = viewModel.state.selectedImage,
                   contentDescription = "product image",
                   contentScale = ContentScale.Crop,
                   modifier = Modifier
                       .clip(RoundedCornerShape(size = Dimens._8dp))
                       .height(Dimens._400dp),
               ) {
                   val state = painter.state
                   if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                       Image(
                           modifier = Modifier
                               .fillMaxWidth()
                               .height(Dimens._200dp),
                           contentScale = ContentScale.Crop,
                           painter = painterResource(id = R.drawable.ic_gray_bg),
                           contentDescription = "avatar"
                       )
                   } else {
                       SubcomposeAsyncImageContent()
                   }
               }

               Row(
                   modifier = Modifier
                       .fillMaxWidth(),
                   horizontalArrangement = Arrangement.End,
               ) {
                   TextButton(
                       onClick = {  viewModel.onEvent(ProductEvent.TogglePreviewDialog) },
                       modifier = Modifier.padding(Dimens._12dp),
                   ) {
                       Text("Dismiss")
                   }
               }
           }
        }
    }
}