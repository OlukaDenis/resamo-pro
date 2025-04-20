package com.dennytech.resamopro.ui.screen.main.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.user.GetStoreUsersUseCase
import com.dennytech.domain.usecases.user.GetUsersUseCase
import com.dennytech.domain.usecases.user.ToggleUserActivationUseCase
import com.dennytech.resamopro.ui.models.events.UserEvent
import com.dennytech.resamopro.ui.models.states.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val toggleUserActivationUseCase: ToggleUserActivationUseCase,
    private val getSelectedStoreUseCase: GetSelectedStoreUseCase,
    private val getStoreUsersUseCase: GetStoreUsersUseCase
): ViewModel() {

    init {
        onEvent(UserEvent.GetUsers)
        getCurrentStore()
    }

    var error by mutableStateOf("")
    var loading by mutableStateOf(false)
    var state by mutableStateOf(UserState())

    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.GetUsers -> {
                getUsers()
            }

            is UserEvent.ToggleUserActivation -> {
                toggleUserActivation(userId = event.userId, userStatus = event.userStatus)
            }
        }
    }

    private fun getCurrentStore() {
        viewModelScope.launch {
            getSelectedStoreUseCase().collect {
                state = state.copy(currentStore = it)
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            getStoreUsersUseCase()
                .collect {
                    when(it) {
                        is Resource.Success -> {
                            loading = false
                            state = state.copy(storeUsers = it.data)
                        }
                        is Resource.Error -> {
                            error = it.exception
                            loading = false
                        }
                        is Resource.Loading -> loading = true
                    }
                }
        }
    }

    private fun toggleUserActivation(userId: String, userStatus: Int) {
        viewModelScope.launch {
            toggleUserActivationUseCase(
                ToggleUserActivationUseCase.Param(
                    userId = userId,
                    userStatus = userStatus
                )
            ).collect {
                when(it) {
                    is Resource.Success -> {
                        loading = false
                        onEvent(UserEvent.GetUsers)
                    }
                    is Resource.Error -> {
                        error = it.exception
                        loading = false
                    }
                    is Resource.Loading -> loading = true
                }
            }
        }
    }


}
