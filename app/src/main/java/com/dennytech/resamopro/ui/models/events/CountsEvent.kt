package com.dennytech.resamopro.ui.models.events

sealed class CountsEvent {
    data class StartDateChanged(val value: String): CountsEvent()
    data class EndDateChanged(val value: String): CountsEvent()
    data object SubmitFilter: CountsEvent()
    data object GetPopularTypes: CountsEvent()
}