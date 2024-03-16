package com.dennytech.resamopro.ui.screen.main.users

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.user.GetUsersUseCase
import com.dennytech.domain.usecases.user.ToggleUserActivationUseCase
import com.dennytech.resamopro.ui.screen.main.products.ProductEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val toggleUserActivationUseCase: ToggleUserActivationUseCase
): ViewModel() {

    init {
        onEvent(UserEvent.GetUsers)
    }

    private val _usersState: MutableStateFlow<PagingData<UserDomainModel>> =
        MutableStateFlow(value = PagingData.empty())
    val usersState get() = _usersState
    var error by mutableStateOf("")
    var loading by mutableStateOf(false)

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

    private fun getUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _usersState.value = it
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

sealed class UserEvent {
    data object GetUsers: UserEvent()
    data class ToggleUserActivation(
        val userId: String, val userStatus: Int
    ): UserEvent()
}