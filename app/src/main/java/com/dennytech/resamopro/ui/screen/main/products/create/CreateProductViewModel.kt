package com.dennytech.resamopro.ui.screen.main.products.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.products.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
): ViewModel() {
    var state by mutableStateOf(CreateProductState())
    var uploadComplete by mutableStateOf(false)
    
    fun onEvent(event: CreateProductEvent) {
        when(event) {
            is CreateProductEvent.SetImageFile -> {
                state = state.copy(imageUri  = event.file, error = "", imageError = "")
            }

            is CreateProductEvent.NameChanged -> {
                state = state.copy(name = event.value, nameError = "", dirty = false)
            }

            is CreateProductEvent.PriceChanged -> {
                state = state.copy(price = event.value.trim(), priceError = "", dirty = false)
            }

            is CreateProductEvent.ColorChanged -> {
                state = state.copy(color = event.value.trim(), colorError = "", dirty = false)
            }

            is CreateProductEvent.SizeChanged -> {
                state = state.copy(size = event.value.trim(), sizeError = "", dirty = false)
            }

            is CreateProductEvent.BrandChanged -> {
                state = state.copy(brand = event.value.trim(), brandError = "", dirty = false)
            }

            is CreateProductEvent.TypeChanged -> {
                state = state.copy(type = event.value.trim(), typeError = "", dirty = false)
            }

            is CreateProductEvent.Reset -> {
                state = CreateProductState()
            }

            is CreateProductEvent.Submit -> {
                validate()

                if (!state.dirty) {
                    if (state.imageUri == null) {
                        state = state.copy(error = "Select product image")
                    } else {
                        saveProduct()
                    }
                }
            }
        }
    }

    private fun saveProduct() {

        viewModelScope.launch {
            
            val param = CreateProductUseCase.Param(
                brand = state.brand,
                size = state.size,
                fileUri = state.imageUri!!,
                color = state.color,
                type = state.type,
                name = state.name,
                price = state.price
            )
            
            createProductUseCase(param).collect {
                when(it) {
                    is Resource.Loading -> state = state.copy(loading = true)
                    is Resource.Success -> {
                        state = state.copy(
                            loading = false,
                            error = ""
                        )
                        uploadComplete = true
                    }

                    is Resource.Error -> {
                        state = state.copy(
                            loading = false,
                            error = it.exception
                        )
                        uploadComplete = false
                    }
                }
            }
        }
    }

    private fun validate() {

        if (state.name.isEmpty()) {
            state = state.copy(nameError = "Field required", dirty = true)
        }

        if (state.size.isEmpty()) {
            state = state.copy(sizeError = "Field required", dirty = true)
        }

        if (state.color.isEmpty()) {
            state = state.copy(colorError = "Field required", dirty = true)
        }

        if (state.type.isEmpty()) {
            state = state.copy(typeError = "Field required", dirty = true)
        }

        if (state.brand.isEmpty()) {
            state = state.copy(brandError = "Field required", dirty = true)
        }

        if (state.price.isEmpty()) {
            state = state.copy(priceError = "Field required", dirty = true)
        }
    }
}

data class CreateProductState(
    val imageUri: Uri? = null,
    val imageError: String = "",
    val name: String = "",
    val nameError: String = "",
    val size: String = "",
    val sizeError: String = "",
    val color: String = "",
    val colorError: String = "",
    val type: String = "",
    val typeError: String = "",
    val brand: String = "",
    val brandError: String = "",
    val price: String = "",
    val priceError: String = "",
    val dirty: Boolean = false,
    val error: String = "",
    val loading: Boolean = false
)

sealed class CreateProductEvent {
    data class SetImageFile(val file: Uri?): CreateProductEvent()
    data class NameChanged(val value: String): CreateProductEvent()
    data class SizeChanged(val value: String): CreateProductEvent()
    data class ColorChanged(val value: String): CreateProductEvent()
    data class TypeChanged(val value: String): CreateProductEvent()
    data class BrandChanged(val value: String): CreateProductEvent()
    data class PriceChanged(val value: String): CreateProductEvent()
    data object Submit: CreateProductEvent()
    data object Reset: CreateProductEvent()
}