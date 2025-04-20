package com.dennytech.resamopro.ui.screen.main.users

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
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.UserItem
import com.dennytech.resamopro.ui.models.events.UserEvent
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

        Column(
            modifier = Modifier.padding(values)
        ) {

            Column(
                modifier = Modifier.padding(Dimens._0dp)
            ) {

                if (viewModel.loading) {
                    Column(modifier = Modifier.padding(vertical = Dimens._30dp)) {
                        LoadingCircle()
                    }
                }

                val users = viewModel.state.storeUsers
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = Dimens._16dp, vertical = Dimens._14dp),
                ) {
                    items(users) { item ->
                        UserItem(
                            user = item,
                            onClick = {},
                            onMenuClick = {
                                viewModel.onEvent(
                                    UserEvent.ToggleUserActivation(
                                    userId = item.id,
                                    userStatus = item.status
                                ))
                            }
                        )
                    }
                }
            }

        }
    }
    }