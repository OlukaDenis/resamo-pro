package com.dennytech.resamopro.ui.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.repository.PreferenceRepository
import com.dennytech.domain.usecases.auth.LoginUseCase
import com.dennytech.resamopro.ui.models.events.LoginEvent
import com.dennytech.resamopro.ui.models.states.LoginState
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
                    login()
                }
            }

            else -> {

            }
        }
    }

    private fun login() {
        viewModelScope.launch {

            val param = LoginUseCase.Param(
                email = formState.email,
                password = formState.password
            )

            loginUseCase(param).collect {
                when(it) {
                    is Resource.Loading -> formState = formState.copy(loading = true)
                    is Resource.Success -> {
                        formState = formState.copy(
                            loading = false,
                            error = "",
                            user = it.data
                        )
                        loginComplete = true
                    }

                    is Resource.Error -> {
                        formState = formState.copy(
                            loading = false,
                            errorDialog = true,
                            error = it.exception
                        )
                        loginComplete = false
                    }
                }
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
