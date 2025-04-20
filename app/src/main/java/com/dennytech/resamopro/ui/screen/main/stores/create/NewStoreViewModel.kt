package com.dennytech.resamopro.ui.screen.main.stores.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.store.NewStoreUseCase
import com.dennytech.resamopro.ui.models.events.CreateStoreEvent
import com.dennytech.resamopro.ui.models.states.CreateStoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateStoreViewModel @Inject constructor(
    private val newStoreUseCase: NewStoreUseCase
): ViewModel() {

    var state by mutableStateOf(CreateStoreState())
    var complete by mutableStateOf(false)
    var fromLogin by mutableStateOf(false)

    fun onEvent(event: CreateStoreEvent) {
        when(event) {
            is CreateStoreEvent.NameChanged -> {
                state = state.copy(name = event.value, nameError = "", dirty = false)
            }
            is CreateStoreEvent.LocationChanged -> {
                state = state.copy(location = event.value, locationError = "", dirty = false)
            }
            is CreateStoreEvent.CategoryChanged -> {
                state = state.copy(category = event.value, categoryError = "", dirty = false)
            }
            is CreateStoreEvent.DescriptionChanged -> {
                state = state.copy(description = event.value, descriptionError = "", dirty = false)
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
                        Timber.d("shop creation success")
                        state = state.copy(
                            loading = false,
                            error = "",
                            showSuccessDialog = true
                        )
                        complete = true
                    }

                    is Resource.Error -> {
                        Timber.d("shop creation failed")
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
        if (fromLogin && state.category.isNullOrEmpty()) {
            state = state.copy(categoryError = "Category required", dirty = true)
        }

    }
}


