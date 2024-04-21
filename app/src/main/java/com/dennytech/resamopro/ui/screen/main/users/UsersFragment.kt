package com.dennytech.resamopro.ui.screen.main.users

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dennytech.data.utils.resolveError
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.LoadingMore
import com.dennytech.resamopro.ui.components.UserItem
import com.dennytech.resamopro.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersFragment(
    viewModel: UserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    navigateToNewUser: () -> Unit,
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
                        text = stringResource(R.string.users),
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
                        if(it.isAdmin()) {
                            IconButton(onClick = { navigateToNewUser() }) {
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
    ) {values ->

        if (viewModel.loading) {
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

                val products: LazyPagingItems<UserDomainModel> =
                    viewModel.usersState.collectAsLazyPagingItems()
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = Dimens._16dp, vertical = Dimens._14dp),
                ) {
                    items(
                        products.itemCount,
                    ) { index ->
                        val item = products[index]!!
                        UserItem(
                            user = item,
                            onClick = {},
                            onMenuClick = {
                                viewModel.onEvent(UserEvent.ToggleUserActivation(
                                    userId = item.id,
                                    userStatus = item.status
                                ))
                            }
                        )
                    }

                    products.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                               item { LoadingCircle() }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = products.loadState.refresh as LoadState.Error
                                item {
                                    ErrorLabel(
                                        message = error.error.resolveError(),
                                        retry = true,
                                        onRetry = {retry()}
                                    )
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                               item {
                                   LoadingMore()
                               }
                            }

                            loadState.append is LoadState.Error -> {
                                val error = products.loadState.append as LoadState.Error
                                item {
                                    ErrorLabel(
                                        message = error.error.resolveError(),
                                        retry = true,
                                        onRetry = {retry()}
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    }