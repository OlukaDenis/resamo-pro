package com.dennytech.resamopro.ui.screen.main.users.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
): ViewModel() {

    var state by mutableStateOf(CreateUserState())
    var complete by mutableStateOf(false)

    fun onEvent(event: CreateUserEvent) {
        when(event) {
            is CreateUserEvent.ToggleSuccessDialog -> {
//                state = state.copy(showSuccessDialog = !state.showSuccessDialog)
                state = CreateUserState()
            }

            is CreateUserEvent.EmailChanged -> {
                state = state.copy(email = event.value.trim(), emailError = "", dirty = false)
            }

            is CreateUserEvent.PasswordChanged -> {
                state = state.copy(password = event.value.trim(), passwordError = "", dirty = false)
            }

            is CreateUserEvent.PhoneChanged -> {
                state = state.copy(phone = event.value.trim(), phoneError = "", dirty = false)
            }

            is CreateUserEvent.FirstNameChanged -> {
                state = state.copy(firstName = event.value.trim(), firstNameError = "", dirty = false)
            }


            is CreateUserEvent.LastNameChanged -> {
                state = state.copy(lastName = event.value.trim(), lastNameError = "", dirty = false)
            }

            is CreateUserEvent.Submit -> {
               validate()

                if (!state.dirty) {
                    createUser()
                }
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch {

            val param = CreateUserUseCase.Param(
               firstName = state.firstName,
                lastName = state.lastName,
                phone = state.phone,
                email = state.email,
                password = state.password
            )

            createUserUseCase(param).collect {
                when(it) {
                    is Resource.Loading -> state = state.copy(loading = true)
                    is Resource.Success -> {
                        state = state.copy(
                            loading = false,
                            error = "",
                            showSuccessDialog = true
                        )
                        complete = true
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            loading = false,
                            error = it.exception
                        )
                        complete = false
                    }
                }
            }
        }
    }

    private fun validate() {

        if (state.firstName.isEmpty()) {
            state = state.copy(firstNameError = "Field required", dirty = true)
        }

        if (state.lastName.isEmpty()) {
            state = state.copy(lastNameError = "Field required", dirty = true)
        }

        if (state.phone.isEmpty()) {
            state = state.copy(phoneError = "Field required", dirty = true)
        }

        if (state.email.isEmpty()) {
            state = state.copy(emailError = "Field required", dirty = true)
        }

        if (state.password.isEmpty()) {
            state = state.copy(passwordError = "Field required", dirty = true)
        }
    }
}

data class CreateUserState(
    val email: String = "",
    val emailError: String = "",
    val phone: String = "",
    val phoneError: String = "",
    val firstName: String = "",
    val firstNameError: String = "",
    val lastName: String = "",
    val lastNameError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val dirty: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorDialog: Boolean = false,
)

sealed class CreateUserEvent {
    data object ToggleSuccessDialog: CreateUserEvent()
    data class FirstNameChanged(val value: String): CreateUserEvent()
    data class LastNameChanged(val value: String): CreateUserEvent()
    data class PhoneChanged(val value: String): CreateUserEvent()
    data class EmailChanged(val value: String): CreateUserEvent()
    data class PasswordChanged(val value: String): CreateUserEvent()
    data object Submit: CreateUserEvent()
}