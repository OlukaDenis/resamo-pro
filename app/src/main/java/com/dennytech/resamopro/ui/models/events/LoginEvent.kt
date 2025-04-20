package com.dennytech.resamopro.ui.models.events

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object DismissErrorDialog: LoginEvent()
    data object Submit : LoginEvent()
}