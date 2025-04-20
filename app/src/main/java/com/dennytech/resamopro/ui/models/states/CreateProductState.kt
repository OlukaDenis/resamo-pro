package com.dennytech.resamopro.ui.models.states

import android.net.Uri
import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.resamopro.models.KeyValueModel

data class CreateProductState(
    val productId: String = "",
    val imageUri: Uri? = null,
    val imageError: String = "",
    val name: String = "",
    val nameError: String = "",
    val size: String = "",
    val color: String = "",
    val type: KeyValueModel? = null,
    val typeError: String = "",
    val brand: String = "",
    val brandError: String = "",
    val price: String = "",
    val priceError: String = "",
    val dirty: Boolean = false,
    val damaged: Boolean = false,
    val error: String = "",
    val loading: Boolean = false,
    val updateComplete: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val category: KeyValueModel? = null,
    val categoryError: String = "",
    val quantity: String = "1",
    val quantityError: String = "",
    val currentStore: StoreDomainModel? = null,
    val productTypes: List<KeyValueModel> = emptyList(),
    val productCategories: List<KeyValueModel> = emptyList()
)
