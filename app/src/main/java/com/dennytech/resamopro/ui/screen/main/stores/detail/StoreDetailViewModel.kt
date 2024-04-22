package com.dennytech.resamopro.ui.screen.main.stores.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.store.AssignUserToStoreUseCase
import com.dennytech.domain.usecases.store.GetStoreByIdUseCase
import com.dennytech.domain.usecases.store.UnAssignedStoreUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreByIdUseCase: GetStoreByIdUseCase,
    private val unAssignedStoreUsersUseCase: UnAssignedStoreUsersUseCase,
    private val assignUserToStoreUseCase: AssignUserToStoreUseCase
): ViewModel() {

    var state by mutableStateOf(StoreDetailState())

    fun onEvent(event: StoreDetailEvent) {
        when(event) {
            is StoreDetailEvent.GetStore -> getStore(event.storeId)

            is StoreDetailEvent.FetchUnAssignedUsers -> fetchUnassignedUsers(event.storeId)

            is StoreDetailEvent.ToggleUnAssignedDialog -> {
                state = state.copy(showUnAssignedDialog = !state.showUnAssignedDialog)
            }

            is StoreDetailEvent.AssignUser -> assignUserToStore(event.userId, event.storeId)
        }
    }

    private fun fetchUnassignedUsers(storeId: String) {
        viewModelScope.launch {
            unAssignedStoreUsersUseCase(UnAssignedStoreUsersUseCase.Param(storeId = storeId)).collect {
                state = when(it) {
                    is Resource.Loading -> {
                        state.copy(loading = true)
                    }

                    is Resource.Error -> {
                        state.copy(loading = false)
                    }

                    is Resource.Success -> {
                        state.copy(loading = false, unassignedUsers = it.data)
                    }
                }
            }
        }
    }

    private fun assignUserToStore(userId: String, storeId: String) {
        viewModelScope.launch {
            assignUserToStoreUseCase(AssignUserToStoreUseCase.Param(userId = userId, storeId = storeId)).collect {
            when(it) {
                    is Resource.Loading -> {
                        state = state.copy(assigningUser = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(assigningUser = false)
                    }

                    is Resource.Success -> {
                        getStore(storeId)
                        state = state.copy(assigningUser = false, showUnAssignedDialog = false)
                    }
                }
            }
        }
    }

    private fun getStore(storeId: String) {
        viewModelScope.launch {
            getStoreByIdUseCase(GetStoreByIdUseCase.Param(storeId = storeId)).collect {
                state = state.copy(store = it)
            }
        }
    }
}

data class StoreDetailState(
    val store: StoreDomainModel? = null,
    val unassignedUsers: List<UserDomainModel> = emptyList(),
    val loading: Boolean = false,
    val showUnAssignedDialog: Boolean = false,
    val assigningUser: Boolean = false
)

sealed class StoreDetailEvent {
    data class GetStore(val storeId: String): StoreDetailEvent()
    data object ToggleUnAssignedDialog: StoreDetailEvent()
    data class FetchUnAssignedUsers(val storeId: String): StoreDetailEvent()
    data class AssignUser(val userId: String, val storeId: String): StoreDetailEvent()
}