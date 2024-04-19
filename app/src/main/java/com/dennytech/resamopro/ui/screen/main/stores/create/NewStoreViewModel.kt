package com.dennytech.resamopro.ui.screen.main.stores.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.store.NewStoreUseCase
import com.dennytech.domain.usecases.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateStoreViewModel @Inject constructor(
    private val newStoreUseCase: NewStoreUseCase
): ViewModel() {

    var state by mutableStateOf(CreateStoreState())
    var complete by mutableStateOf(false)

    fun onEvent(event: CreateStoreEvent) {
        when(event) {
            is CreateStoreEvent.NameChanged -> {
                state = state.copy(name = event.value.trim(), nameError = "", dirty = false)
            }
            is CreateStoreEvent.LocationChanged -> {
                state = state.copy(location = event.value.trim(), locationError = "", dirty = false)
            }
            is CreateStoreEvent.CategoryChanged -> {
                state = state.copy(category = event.value.trim(), categoryError = "", dirty = false)
            }
            is CreateStoreEvent.DescriptionChanged -> {
                state = state.copy(description = event.value.trim(), descriptionError = "", dirty = false)
            }

            is CreateStoreEvent.Submit -> {
                validate()

                if (!state.dirty) {
                    newStore()
                }
            }
        }
    }

    private fun newStore() {
        viewModelScope.launch {
            val param = NewStoreUseCase.Param(
               name = state.name,
                category = state.category,
                location = state.location,
                description = state.description.ifEmpty { null }
            )

            newStoreUseCase(param).collect {
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
        if (state.name.isEmpty()) {
            state = state.copy(nameError = "Name required", dirty = true)
        }

        if (state.location.isEmpty()) {
            state = state.copy(locationError = "Location required", dirty = true)
        }
        if (state.category.isEmpty()) {
            state = state.copy(categoryError = "Category required", dirty = true)
        }

    }
}

data class CreateStoreState(
    val name: String = "",
    val nameError: String = "",
    val description: String = "",
    val descriptionError: String = "",
    val location: String = "",
    val locationError: String = "",
    val category: String = "",
    val categoryError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val dirty: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorDialog: Boolean = false,
)

sealed class CreateStoreEvent {
    data class NameChanged(val value: String): CreateStoreEvent()
    data class LocationChanged(val value: String): CreateStoreEvent()
    data class CategoryChanged(val value: String): CreateStoreEvent()
    data class DescriptionChanged(val value: String): CreateStoreEvent()
    data object Submit: CreateStoreEvent()
}