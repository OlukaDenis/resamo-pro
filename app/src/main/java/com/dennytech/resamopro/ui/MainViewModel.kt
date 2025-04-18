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
import com.dennytech.domain.usecases.store.GetSelectedStoreUseCase
import com.dennytech.domain.usecases.store.GetUserStoreListUseCase
import com.dennytech.resamopro.models.KeyValueModel
import com.dennytech.resamopro.utils.Helpers.capitalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserStoreListUseCase: GetUserStoreListUseCase,
    private val getSelectedStoreUseCase: GetSelectedStoreUseCase,
) : ViewModel() {

    var state by mutableStateOf(MainState())

    init {
        initiate()
    }

    fun initiate() {
        getCurrentUser()
        getUserStores()
        getCurrentStore()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val userResult = runCatching { getCurrentUserUseCase() }

            if (userResult.isSuccess) {
                state = state.copy(user = userResult.getOrNull())
            }
        }
    }

    fun refreshUser() {
        getCurrentUser()
    }

    private fun getUserStores() {
        viewModelScope.launch {
            getUserStoreListUseCase().collect {
                state = state.copy(userStores = it)
            }
        }
    }

    private fun getCurrentStore() {
        viewModelScope.launch {
            getSelectedStoreUseCase().collect { store ->
                val types = if (store.productTypes.isNotEmpty())
                    store.productTypes.map { KeyValueModel(it, it.capitalize()) }
                else emptyList()

                val categories = if (store.categories.isNotEmpty())
                    store.categories.map { KeyValueModel(it.id, it.name) }
                else emptyList()

                state = state.copy(currentStore = store, productTypes = types, productCategories = categories)

//                Timber.d(
//                    "Current store: " + store + "\n" +
//                            "Current types: " + types + "\n" +
//                            "Categories: " + categories
//                )
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
    val userStores: List<StoreDomainModel> = emptyList(),
    val currentStore: StoreDomainModel? = null,
    val productTypes: List<KeyValueModel> = emptyList(),
    val productCategories: List<KeyValueModel> = emptyList()
)