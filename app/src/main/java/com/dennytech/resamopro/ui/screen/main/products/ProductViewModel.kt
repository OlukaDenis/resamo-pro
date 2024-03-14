package com.dennytech.resamopro.ui.screen.main.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dennytech.domain.models.ProductDomainModel
import com.dennytech.domain.usecases.products.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _productsState: MutableStateFlow<PagingData<ProductDomainModel>> = MutableStateFlow(value = PagingData.empty())
    val productsState get() = _productsState


    init {
        onEvent(ProductEvent.GetProducts)
    }


    fun onEvent(event: ProductEvent) {
        when(event) {

            is ProductEvent.GetProducts -> {
                getProducts()
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            getProductsUseCase(param = GetProductsUseCase.Param(
                inStock = true
            ))
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _productsState.value = it
                }
        }
    }
}

sealed class ProductEvent {
    data object GetProducts: ProductEvent()
}