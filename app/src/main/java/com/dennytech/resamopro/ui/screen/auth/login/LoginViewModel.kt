package com.dennytech.resamopro.ui.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.usecases.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    var formState by mutableStateOf(LoginState())
    var loginComplete by mutableStateOf(false)


    init {

    }

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> {
                formState = formState.copy(email = event.email.trim(), error = "")
            }

            is LoginEvent.PasswordChanged -> {
                formState = formState.copy(password = event.password.trim(), error = "")
            }

            is LoginEvent.Submit -> {
                if (formState.email.isEmpty()) {
                    formState = formState.copy(error = "Email required")
                } else if (formState.password.isEmpty()) {
                    formState = formState.copy(error = "Password required")
                }  else {

                }
            }

            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

data class LoginState(
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val errorDialog: Boolean = false,
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object DismissErrorDialog: LoginEvent()
    data object Submit : LoginEvent()
}