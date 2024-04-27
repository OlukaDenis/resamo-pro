package com.dennytech.resamopro.ui.screen.main.users

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.store.SetSelectedStoreUseCase
import com.dennytech.domain.usecases.user.GetStoreUsersUseCase
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

data class UserState(
    val currentStore: StoreDomainModel? = null,
    val storeUsers: List<UserDomainModel> = emptyList()
)

sealed class UserEvent {
    data object GetUsers: UserEvent()
    data class ToggleUserActivation(
        val userId: String, val userStatus: Int
    ): UserEvent()
}