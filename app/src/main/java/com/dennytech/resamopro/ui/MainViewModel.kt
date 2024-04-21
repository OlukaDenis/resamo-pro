package com.dennytech.resamopro.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.LogoutUseCase
import com.dennytech.domain.usecases.account.GetCurrentUserUseCase
import com.dennytech.domain.usecases.store.GetUserStoreListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserStoreListUseCase: GetUserStoreListUseCase,
) : ViewModel() {

    var state by mutableStateOf(MainState())

    init {
        getCurrentUser()
        getUserStores()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val userResult = runCatching { getCurrentUserUseCase() }

            if (userResult.isFailure) {
                state = state.copy(error = "User not found")
            }

            if (userResult.isSuccess) {
                state = state.copy(user = userResult.getOrNull())
            }
        }
    }

    private fun getUserStores() {
        viewModelScope.launch {
            getUserStoreListUseCase().collect {
                state = state.copy(userStores = it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}

data class MainState(
    val error: String = "",
    val user: UserDomainModel? = null,
    val userStores: List<StoreDomainModel> = emptyList()
)