package com.dennytech.resamopro.ui.screen.main.stores.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dennytech.domain.models.UserDomainModel.Companion.isOwner
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.store.StoreItem
import com.dennytech.resamopro.ui.navigation.MainScreen
import com.dennytech.resamopro.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListFragment(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    navigateUp: () -> Unit,
    navigateToNewStore: () -> Unit,
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
                        text = stringResource(R.string.stores),
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
                    mainViewModel.state.user?.let {
                        if(it.isOwner()) {
                            IconButton(onClick = {  }) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = stringResource(id = R.string.add),
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { values ->

        Column(
            modifier = Modifier.padding(values)
        ) {

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {

                val stores = mainViewModel.state.userStores

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(stores) {
                        StoreItem(
                            store = it,
                            onMenuClick = {},
                            onClick = { goToStoreDetail(navController, it.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun goToStoreDetail(navController: NavController, storeId: String) {
    navController.navigate(
        MainScreen.StoreDetail.route
            .replace(
                oldValue = "{storeId}",
                newValue = storeId
            )
    )
}
