package com.dennytech.resamopro.ui.models.events

sealed class StoreDetailEvent {
    data class Init(val storeId: String): StoreDetailEvent()
    data class GetStore(val storeId: String): StoreDetailEvent()
    data object ToggleUnAssignedDialog: StoreDetailEvent()
    data object ToggleCreateTypeDialog: StoreDetailEvent()
    data object ToggleCreateCategoryDialog: StoreDetailEvent()
    data class FetchUnAssignedUsers(val storeId: String): StoreDetailEvent()
    data class AssignUser(val userId: String, val storeId: String): StoreDetailEvent()
    data class TypeChanged(val value: String): StoreDetailEvent()
    data class CategoryNameChanged(val value: String): StoreDetailEvent()
    data class CategoryDescriptionChanged(val value: String): StoreDetailEvent()
    data object SubmitType: StoreDetailEvent()
    data object SubmitCategory: StoreDetailEvent()
}