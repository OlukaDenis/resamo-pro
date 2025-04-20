package com.dennytech.resamopro.ui.models.events

sealed class CreateStoreEvent {
    data class NameChanged(val value: String): CreateStoreEvent()
    data class LocationChanged(val value: String): CreateStoreEvent()
    data class CategoryChanged(val value: String): CreateStoreEvent()
    data class DescriptionChanged(val value: String): CreateStoreEvent()
    data object Submit: CreateStoreEvent()
}