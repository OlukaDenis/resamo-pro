package com.dennytech.resamopro.ui.models.events

sealed class CreateUserEvent {
    data object ToggleSuccessDialog: CreateUserEvent()
    data class FirstNameChanged(val value: String): CreateUserEvent()
    data class LastNameChanged(val value: String): CreateUserEvent()
    data class PhoneChanged(val value: String): CreateUserEvent()
    data class EmailChanged(val value: String): CreateUserEvent()
    data class PasswordChanged(val value: String): CreateUserEvent()
    data object Submit: CreateUserEvent()
}