package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel

data class UserState(
    val currentStore: StoreDomainModel? = null,
    val storeUsers: List<UserDomainModel> = emptyList()
)
