package com.dennytech.resamopro.ui.screen.main.stores.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CloseIcon
import com.dennytech.resamopro.ui.components.DiamondIcon
import com.dennytech.resamopro.ui.components.EmptyComponent
import com.dennytech.resamopro.ui.components.LeftRightLabel
import com.dennytech.resamopro.ui.components.store.CreateProductCategoryBottomSheet
import com.dennytech.resamopro.ui.components.store.CreateProductTypeBottomSheet
import com.dennytech.resamopro.ui.components.store.StoreUserItem
import com.dennytech.resamopro.ui.components.store.UnAssignedStoreUsersBottomSheet
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.utils.Helpers.capitalize
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailFragment(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    storeId: String
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
                        text = viewModel.state.store?.name.orEmpty(),
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

        LaunchedEffect(true) {
            viewModel.onEvent(StoreDetailEvent.GetStore(storeId))
            viewModel.onEvent(StoreDetailEvent.Init(storeId))
        }

        Column(
            modifier = Modifier.padding(values)
        ) {

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {
                LeftRightLabel(
                    startText = "Product Types",
                    endText = "+ Add type",
                    onActionClick = { viewModel.onEvent(StoreDetailEvent.ToggleCreateTypeDialog)}
                )

                val productTypes = viewModel.state.store?.productTypes.orEmpty()

                if (productTypes.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                    ) {
                        items(productTypes) {
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = Dimens._0dp
                                ),
                                shape = RoundedCornerShape(Dimens._8dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                ),
                                modifier = Modifier
                                    .padding(Dimens._4dp)
                                    .fillMaxWidth()
                            ) {
                                
                                Column(modifier = Modifier.padding(Dimens._16dp)) {
                                    Text(text = it.capitalize())
                                }
                            }
                        }
                    }
                } else {
                    EmptyComponent()
                }

                if (viewModel.state.showCreateTypeDialog) {
                    CreateProductTypeBottomSheet(
                        storeId = storeId,
                        onDismiss = {viewModel.onEvent(StoreDetailEvent.ToggleCreateTypeDialog)}
                    )
                }
                
                LeftRightLabel(
                    startText = "Categories",
                    endText = "+ Add category",
                    onActionClick = {viewModel.onEvent(StoreDetailEvent.ToggleCreateCategoryDialog)}
                )

                val categories = viewModel.state.store?.categories.orEmpty()

                if (categories.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                    ) {
                        items(categories) {
                            Card(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = Dimens._0dp
                                ),
                                shape = RoundedCornerShape(Dimens._8dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                ),
                                modifier = Modifier
                                    .padding(Dimens._4dp)
                                    .fillMaxWidth()
                            ) {

                                Column(modifier = Modifier.padding(Dimens._16dp)) {
                                    Text(text = it.name.capitalize())
                                }
                            }
                        }
                    }
                } else {
                    EmptyComponent()
                }

                if (viewModel.state.showCreateCategoryDialog) {
                    CreateProductCategoryBottomSheet(
                        onDismiss = {viewModel.onEvent(StoreDetailEvent.ToggleCreateCategoryDialog)}
                    )
                }

                LeftRightLabel(
                    startText = "Users",
                    endText = "+ Assign user",
                    onActionClick = {
                        viewModel.onEvent(StoreDetailEvent.FetchUnAssignedUsers(storeId = storeId))
                        viewModel.onEvent(StoreDetailEvent.ToggleUnAssignedDialog)
                    }
                )

                val users = viewModel.state.store?.users.orEmpty()

                if (users.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                    ) {
                        items(users) {
                            StoreUserItem(
                                user = it,
                                onMenuClick = {},
                                onClick = {  }
                            )
                        }
                    }
                } else {
                    EmptyComponent()
                }

                if (viewModel.state.showUnAssignedDialog) {
                    UnAssignedStoreUsersBottomSheet(
                        storeId = storeId,
                        onDismiss = {viewModel.onEvent(StoreDetailEvent.ToggleUnAssignedDialog)}
                    )
                }
            }
        }
    }
}