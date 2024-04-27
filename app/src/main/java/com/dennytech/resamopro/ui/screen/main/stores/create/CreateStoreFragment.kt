package com.dennytech.resamopro.ui.screen.main.stores.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.screen.main.users.create.CreateUserEvent
import com.dennytech.resamopro.ui.theme.Dimens
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStoreFragment(
    viewModel: CreateStoreViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    fromLogin: Boolean,
    navigateToHome: () -> Unit = {},
    navigateToStores: () -> Unit = {}
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
                        text = stringResource(R.string.new_store),
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
                }
            )
        }
    ) { values ->

        LaunchedEffect(viewModel.complete) {
            Timber.d("Action from launched effect....")
            if (viewModel.complete) {
                Timber.d("launched effect complete..")
            }
        }

        viewModel.fromLogin = fromLogin

        if (viewModel.complete) {
            if (fromLogin) navigateToHome() else navigateUp()
        }

        Column(
            modifier = Modifier
                .padding(values)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {

                CustomTextField(
                    value = viewModel.state.name,
                    onValueChange = { viewModel.onEvent(CreateStoreEvent.NameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.name),
                    isError = viewModel.state.nameError.isNotEmpty(),
                    errorMessage = viewModel.state.nameError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.location,
                    onValueChange = { viewModel.onEvent(CreateStoreEvent.LocationChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.location),
                    isError = viewModel.state.locationError.isNotEmpty(),
                    errorMessage = viewModel.state.locationError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                if (fromLogin) {
                    CustomTextField(
                        value = viewModel.state.category.orEmpty(),
                        onValueChange = { viewModel.onEvent(CreateStoreEvent.CategoryChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = stringResource(R.string.category),
                        isError = viewModel.state.categoryError.isNotEmpty(),
                        errorMessage = viewModel.state.categoryError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                    )
                    Spacer(modifier = Modifier.height(Dimens._16dp))
                }

                CustomTextField(
                    value = viewModel.state.description,
                    onValueChange = { viewModel.onEvent(CreateStoreEvent.DescriptionChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.description),
                    isError = viewModel.state.descriptionError.isNotEmpty(),
                    errorMessage = viewModel.state.descriptionError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(Dimens._30dp))

                CustomButton(title = stringResource(R.string.create_store),
                    modifier = Modifier.weight(1f),
                    loading = viewModel.state.loading,
                    onClick = { viewModel.onEvent(CreateStoreEvent.Submit) })

                Spacer(modifier = Modifier.height(Dimens._20dp))
            }
        }
    }
}