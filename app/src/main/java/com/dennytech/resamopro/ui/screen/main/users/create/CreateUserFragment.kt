package com.dennytech.resamopro.ui.screen.main.users.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.dialogs.SuccessDialog
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.screen.main.users.UserEvent
import com.dennytech.resamopro.ui.screen.main.users.UserViewModel
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlueLight900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserFragment(
    viewModel: CreateUserViewModel = hiltViewModel(),
    usersViewModel: UserViewModel = hiltViewModel(),
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
                        text = stringResource(R.string.new_user),
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

        Column(
            modifier = Modifier
                .padding(values)
                .verticalScroll(rememberScrollState())
        ) {

            var passwordVisible by remember { mutableStateOf(false) }

            if (viewModel.state.showSuccessDialog) {
                SuccessDialog(
                    dismissDialog = {
                        viewModel.onEvent(CreateUserEvent.ToggleSuccessDialog)
                        usersViewModel.onEvent(UserEvent.GetUsers)
                    },
                    message = "Successfully created a new user."
                )
            }

            Column(
                modifier = Modifier.padding(Dimens._16dp)
            ) {

                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = Dimens._0dp
                    ),
                    shape = RoundedCornerShape(Dimens._16dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TruliBlueLight900,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column( modifier = Modifier.padding(Dimens._12dp)) {
                        Text(text = "Adding new user to ${viewModel.state.currentStore?.name} store")
                    }
                }

                VerticalSpacer(Dimens._16dp)
                CustomTextField(
                    value = viewModel.state.firstName,
                    onValueChange = { viewModel.onEvent(CreateUserEvent.FirstNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "First Name",
                    isError = viewModel.state.firstNameError.isNotEmpty(),
                    errorMessage = viewModel.state.firstNameError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.lastName,
                    onValueChange = { viewModel.onEvent(CreateUserEvent.LastNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Last Name",
                    isError = viewModel.state.lastNameError.isNotEmpty(),
                    errorMessage = viewModel.state.lastNameError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.email,
                    onValueChange = { viewModel.onEvent(CreateUserEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Email",
                    isError = viewModel.state.emailError.isNotEmpty(),
                    errorMessage = viewModel.state.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.phone,
                    onValueChange = { viewModel.onEvent(CreateUserEvent.PhoneChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Phone Number",
                    isError = viewModel.state.phoneError.isNotEmpty(),
                    errorMessage = viewModel.state.phoneError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._16dp))

                CustomTextField(
                    value = viewModel.state.password,
                    onValueChange = { viewModel.onEvent(CreateUserEvent.PasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Password",
                    isError = viewModel.state.passwordError.isNotEmpty(),
                    errorMessage = viewModel.state.passwordError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        // Localized description for accessibility services
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        // Toggle button to hide or display password
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(Dimens._30dp))

                CustomButton(title = stringResource(R.string.save),
                    modifier = Modifier.weight(1f),
                    loading = viewModel.state.loading,
                    onClick = { viewModel.onEvent(CreateUserEvent.Submit) })

                Spacer(modifier = Modifier.height(Dimens._20dp))
            }

        }
    }
}