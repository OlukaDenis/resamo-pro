package com.dennytech.resamopro.ui.screen.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.components.AuthTop
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.CustomTextField
import com.dennytech.resamopro.ui.components.ErrorLabel
import com.dennytech.resamopro.ui.theme.Dimens

@Composable
fun LoginFragment(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToNewStore: () -> Unit
) {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {

           LaunchedEffect(Unit) {

           }

            if (viewModel.loginComplete) {
                viewModel.formState.user?.let {
                    if (it.isAdmin()) {
                        if (it.stores.isNotEmpty()) {
                            navigateToHome()
                        } else {
                            navigateToNewStore()
                        }
                    } else {
                        if (it.stores.isNotEmpty()) {
                            navigateToHome()
                        } else {
                            viewModel.formState = viewModel.formState.copy(error = "You're not assigned to a store. Please contact your admin.")
                        }
                    }
                }
            }


            var passwordVisible by remember { mutableStateOf(false) }

            AuthTop(
                drawable = R.drawable.ic_diamond,
                title = stringResource(id = R.string.welcome),
                description = stringResource(id = R.string.signin_to_your_account)
            )


            Column(
                modifier = Modifier
                    .padding(Dimens._20dp)
            ) {
                Spacer(modifier = Modifier.height(Dimens._20dp))

                CustomTextField(
                    value = viewModel.formState.email,
                    onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.email),
                    isError = viewModel.formState.emailError.isNotEmpty(),
                    errorMessage = viewModel.formState.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                )
                Spacer(modifier = Modifier.height(Dimens._20dp))

                CustomTextField(
                    value = viewModel.formState.password,
                    onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = stringResource(R.string.password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = viewModel.formState.passwordError.isNotEmpty(),
                    errorMessage = viewModel.formState.passwordError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
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

                Spacer(modifier = Modifier.height(Dimens._20dp))

                if (viewModel.formState.error.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(Dimens._16dp))
                        ErrorLabel(message = viewModel.formState.error)
                    }
                }

                Spacer(modifier = Modifier.height(Dimens._30dp))


                CustomButton(title = stringResource(R.string.sign_in),
                    modifier = Modifier.weight(1f),
                    loading = viewModel.formState.loading,
                    onClick = { viewModel.onEvent(LoginEvent.Submit) })

                Spacer(modifier = Modifier.height(Dimens._50dp))

            }
        }
    }
}