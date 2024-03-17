package com.dennytech.resamopro.ui.screen.main.products.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.models.Resource
import com.dennytech.domain.usecases.products.CreateProductUseCase
import com.dennytech.domain.usecases.products.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase
): ViewModel() {
    var state by mutableStateOf(CreateProductState())
    var uploadComplete by mutableStateOf(false)
    var isUpdate by mutableStateOf(false)
    var selectedProductType by mutableStateOf("")
    
    fun onEvent(event: CreateProductEvent) {
        when(event) {
            is CreateProductEvent.SetImageFile -> {
                state = state.copy(imageUri  = event.file, error = "", imageError = "")
            }

            is CreateProductEvent.ToggleSuccessDialog -> {
//                state = state.copy(showSuccessDialog = !state.showSuccessDialog)
                state = CreateProductState()
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
                state = state.copy(brand = event.value, brandError = "", dirty = false)
            }

            is CreateProductEvent.TypeChanged -> {
                state = state.copy(type = event.value.trim(), typeError = "", dirty = false)
            }

            is CreateProductEvent.Reset -> {
                state = CreateProductState()
            }

            is CreateProductEvent.SetProductState -> {
                state = event.value
            }

            is CreateProductEvent.Submit -> {
                validate()

                if (!state.dirty) {
                    if (!isUpdate && state.imageUri == null) {
                        state = state.copy(error = "Select product image")
                    } else {
                        if (isUpdate) updateProduct() else saveProduct()
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
                            error = "",
                            showSuccessDialog = true
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

    private fun updateProduct() {
        viewModelScope.launch {

            val param = UpdateProductUseCase.Param(
                brand = state.brand.ifEmpty { null },
                size = state.size.ifEmpty { null },
                fileUri = state.imageUri,
                color = state.color.ifEmpty { null },
                type = state.type.ifEmpty { null },
                name = state.name.ifEmpty { null },
                price = state.price.ifEmpty { null },
                productId = state.productId
            )

            updateProductUseCase(param).collect {
                when(it) {
                    is Resource.Loading -> state = state.copy(loading = true)
                    is Resource.Success -> {
                        state = state.copy(
                            loading = false,
                            error = "",
                            showSuccessDialog = true
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
    val productId: String = "",
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
    val loading: Boolean = false,
    val showSuccessDialog: Boolean = false
)

sealed class CreateProductEvent {
    data class SetImageFile(val file: Uri?): CreateProductEvent()
    data class SetProductState(val value: CreateProductState): CreateProductEvent()
    data object ToggleSuccessDialog: CreateProductEvent()
    data class NameChanged(val value: String): CreateProductEvent()
    data class SizeChanged(val value: String): CreateProductEvent()
    data class ColorChanged(val value: String): CreateProductEvent()
    data class TypeChanged(val value: String): CreateProductEvent()
    data class BrandChanged(val value: String): CreateProductEvent()
    data class PriceChanged(val value: String): CreateProductEvent()
    data object Submit: CreateProductEvent()
    data object Reset: CreateProductEvent()
}