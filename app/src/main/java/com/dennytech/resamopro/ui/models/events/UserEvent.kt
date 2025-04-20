package com.dennytech.resamopro.ui.models.events

sealed class UserEvent {
    data object GetUsers: UserEvent()
    data class ToggleUserActivation(
        val userId: String, val userStatus: Int
    ): UserEvent()
}