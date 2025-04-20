package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel

data class StoreDetailState(
    val store: StoreDomainModel? = null,
    val selectedStore: String = "",
    val unassignedUsers: List<UserDomainModel> = emptyList(),
    val loading: Boolean = false,
    val showUnAssignedDialog: Boolean = false,
    val assigningUser: Boolean = false,
    val type: String = "",
    val typeError: String = "",
    val categoryName: String = "",
    val categoryNameError: String = "",
    val categoryDescription: String = "",
    val showCreateTypeDialog: Boolean = false,
    val showCreateCategoryDialog: Boolean = false
)
