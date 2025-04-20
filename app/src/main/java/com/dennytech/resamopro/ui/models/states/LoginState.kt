package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.UserDomainModel

data class LoginState(
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val error: String = "",
    val errorDialog: Boolean = false,
    val user: UserDomainModel? = null
)
