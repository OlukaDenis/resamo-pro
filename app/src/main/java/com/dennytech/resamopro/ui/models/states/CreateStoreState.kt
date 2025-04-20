package com.dennytech.resamopro.ui.models.states

data class CreateStoreState(
    val name: String = "",
    val nameError: String = "",
    val description: String = "",
    val descriptionError: String = "",
    val location: String = "",
    val locationError: String = "",
    val category: String? = null,
    val categoryError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val dirty: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorDialog: Boolean = false,
)
