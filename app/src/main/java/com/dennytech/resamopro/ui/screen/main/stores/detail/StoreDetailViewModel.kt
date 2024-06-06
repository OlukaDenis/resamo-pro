package com.dennytech.resamopro.ui.screen.main.stores.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.domain.usecases.products.CreateProductCategoryUseCase
import com.dennytech.domain.usecases.products.CreateProductTypeUseCase
import com.dennytech.domain.usecases.store.AssignUserToStoreUseCase
import com.dennytech.domain.usecases.store.GetStoreByIdUseCase
import com.dennytech.domain.usecases.store.UnAssignedStoreUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreByIdUseCase: GetStoreByIdUseCase,
    private val unAssignedStoreUsersUseCase: UnAssignedStoreUsersUseCase,
    private val assignUserToStoreUseCase: AssignUserToStoreUseCase,
    private val createProductTypeUseCase: CreateProductTypeUseCase,
    private val createProductCategoryUseCase: CreateProductCategoryUseCase
): ViewModel() {

    var state by mutableStateOf(StoreDetailState())
    var userIdSelected by mutableStateOf("")

    fun onEvent(event: StoreDetailEvent) {
        when(event) {
            is StoreDetailEvent.Init -> {
                state = state.copy(selectedStore = event.storeId)
            }

            is StoreDetailEvent.GetStore -> getStore(event.storeId)

            is StoreDetailEvent.FetchUnAssignedUsers -> fetchUnassignedUsers(event.storeId)

            is StoreDetailEvent.ToggleUnAssignedDialog -> {
                state = state.copy(showUnAssignedDialog = !state.showUnAssignedDialog)
            }

            is StoreDetailEvent.ToggleCreateTypeDialog -> {
                state = state.copy(showCreateTypeDialog = !state.showCreateTypeDialog)
            }

            is StoreDetailEvent.ToggleCreateCategoryDialog -> {
                state = state.copy(showCreateCategoryDialog = !state.showCreateCategoryDialog)
            }

            is StoreDetailEvent.AssignUser -> assignUserToStore(event.userId, event.storeId)

            is StoreDetailEvent.TypeChanged -> {
                state = state.copy(type = event.value, typeError = "")
            }

            is StoreDetailEvent.CategoryNameChanged -> {
                state = state.copy(categoryName = event.value, categoryNameError = "")
            }

            is StoreDetailEvent.CategoryDescriptionChanged -> {
                state = state.copy(categoryDescription = event.value)
            }

            is StoreDetailEvent.SubmitType -> {
                if (state.type.isEmpty()) {
                    state = state.copy(typeError = "Enter product type")
                } else {
                    createProductType()
                }
            }

            is StoreDetailEvent.SubmitCategory -> {
                if (state.categoryName.isEmpty()) {
                    state = state.copy(categoryNameError = "Enter category name")
                } else {
                    createProductCategory()
                }
            }


        }
    }

    private fun createProductCategory() {
        viewModelScope.launch {

            val param = CreateProductCategoryUseCase.Param(
                storeId = state.selectedStore,
                name = state.categoryName.trim(),
                description = state.categoryDescription.ifEmpty { null }
            )

            createProductCategoryUseCase(param).collect {
                state = when(it) {
                    is Resource.Loading -> {
                        state.copy(loading = true)
                    }

                    is Resource.Error -> {
                        state.copy(loading = false)
                    }

                    is Resource.Success -> {
                        getStore(state.selectedStore)
                        state.copy(
                            loading = false,
                            categoryName = "",
                            categoryDescription = "",
                            showCreateCategoryDialog = false
                        )
                    }
                }
            }
        }
    }

    private fun createProductType() {
        viewModelScope.launch {
            createProductTypeUseCase(CreateProductTypeUseCase.Param(
                storeId = state.selectedStore,
                type = state.type.replace(" ", "-").lowercase(Locale.getDefault())
            )).collect {
                when(it) {
                    is Resource.Loading -> {
                        state = state.copy(loading = true)
                    }

                    is Resource.Error -> {
                        state = state.copy(loading = false)
                    }

                    is Resource.Success -> {
                        getStore(state.selectedStore)
                        state = state.copy(loading = false, type = "", showCreateTypeDialog = false)
                    }
                }
            }
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
        userIdSelected = userId

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
            getStoreByIdUseCase(GetStoreByIdUseCase.Param(storeId = storeId)).collect { store ->
                val item = store.copy(productTypes = store.productTypes.filter { it.isNotEmpty() })
                state = state.copy(store = item)
            }
        }
    }
}

data class StoreDetailState(
    val store: StoreDomainModel? = null,
    val selectedStore: String = "",
    val unassignedUsers: List<UserDomainModel> = emptyList(),
    val loading: Boolean = false,
    val showUnAssignedDialog: Boolean = false,
    val assigningUser: Boolean = false,
    val type: String = "",
    val typeError: String = "",
    val categoryName: String = "",
    val categoryNameError: String = "",
    val categoryDescription: String = "",
    val showCreateTypeDialog: Boolean = false,
    val showCreateCategoryDialog: Boolean = false
)

sealed class StoreDetailEvent {
    data class Init(val storeId: String): StoreDetailEvent()
    data class GetStore(val storeId: String): StoreDetailEvent()
    data object ToggleUnAssignedDialog: StoreDetailEvent()
    data object ToggleCreateTypeDialog: StoreDetailEvent()
    data object ToggleCreateCategoryDialog: StoreDetailEvent()
    data class FetchUnAssignedUsers(val storeId: String): StoreDetailEvent()
    data class AssignUser(val userId: String, val storeId: String): StoreDetailEvent()
    data class TypeChanged(val value: String): StoreDetailEvent()
    data class CategoryNameChanged(val value: String): StoreDetailEvent()
    data class CategoryDescriptionChanged(val value: String): StoreDetailEvent()
    data object SubmitType: StoreDetailEvent()
    data object SubmitCategory: StoreDetailEvent()
}