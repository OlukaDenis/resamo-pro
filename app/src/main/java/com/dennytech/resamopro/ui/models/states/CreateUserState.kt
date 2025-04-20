package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.StoreDomainModel

data class CreateUserState(
    val email: String = "",
    val emailError: String = "",
    val phone: String = "",
    val phoneError: String = "",
    val firstName: String = "",
    val firstNameError: String = "",
    val lastName: String = "",
    val lastNameError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val dirty: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val errorDialog: Boolean = false,
    val currentStore: StoreDomainModel? = null
)

