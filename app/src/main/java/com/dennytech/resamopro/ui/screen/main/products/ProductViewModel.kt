package com.dennytech.resamopro.ui.screen.main.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.usecases.products.GetProductsUseCase
import com.dennytech.resamopro.models.ProductFilerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productsState: MutableStateFlow<PagingData<ProductDomainModel>> =
        MutableStateFlow(value = PagingData.empty())
    val productsState get() = _productsState
    var state by mutableStateOf(ProductState())


    init {
        onEvent(ProductEvent.GetProducts)
    }


    fun onEvent(event: ProductEvent) {
        when (event) {

            is ProductEvent.GetProducts -> {
                getProducts()
            }

            is ProductEvent.ToggleFilterDialog -> {
                state = state.copy(showFilterDialog = !state.showFilterDialog)
            }

            is ProductEvent.TogglePreviewDialog -> {
                state = state.copy(showPreviewDialog = !state.showPreviewDialog)
            }

            is ProductEvent.SetBrandFilter -> {
                state = state.copy(filters = state.filters.copy(brand = event.value))
            }

            is ProductEvent.SetColorFilter -> {
                state = state.copy(filters = state.filters.copy(color = event.value))
            }

            is ProductEvent.SetSizeFilter -> {
                state = state.copy(filters = state.filters.copy(size = event.value))
            }

            is ProductEvent.SetTypeFilter -> {
                state = state.copy(filters = state.filters.copy(type = event.value))
            }

            is ProductEvent.ClearFilters -> {
                state = state.copy(filters = ProductFilerModel())
                onEvent(ProductEvent.GetProducts)
            }

            is ProductEvent.SelectImage -> {
                state = state.copy(selectedImage = event.image)
            }
        }
    }

    private fun getProducts() {
        val filter = state.filters
        val param = GetProductsUseCase.Param(
            brand = filter.brand.trim().replace(" ", "-"),
            color = filter.color.trim(),
            size = filter.size.trim(),
            type = filter.type.trim()
        )

        viewModelScope.launch {
            getProductsUseCase(param = param)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _productsState.value = it
                }
        }
    }
}

data class ProductState(
    val showFilterDialog: Boolean = false,
    val showPreviewDialog: Boolean = false,
    val selectedImage: String = "",
    val filters: ProductFilerModel = ProductFilerModel(),
    val loading: Boolean = false,
    val error: String = ""
)

sealed class ProductEvent {
    data object GetProducts : ProductEvent()
    data object ToggleFilterDialog : ProductEvent()
    data class SelectImage(val image: String): ProductEvent()
    data object TogglePreviewDialog : ProductEvent()
    data class SetBrandFilter(val value: String) : ProductEvent()
    data class SetColorFilter(val value: String) : ProductEvent()
    data class SetTypeFilter(val value: String) : ProductEvent()
    data class SetSizeFilter(val value: String) : ProductEvent()
    data object ClearFilters : ProductEvent()
}