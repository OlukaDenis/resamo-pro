package com.dennytech.resamopro.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.account.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var state by mutableStateOf(MainState())

    init {
        getCurrentUser()
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
}

data class MainState(
    val error: String = "",
    val user: UserDomainModel? = null,
)