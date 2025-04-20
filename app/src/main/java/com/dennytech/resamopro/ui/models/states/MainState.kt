package com.dennytech.resamopro.ui.models.states

import com.dennytech.domain.models.StoreDomainModel
import com.dennytech.domain.models.UserDomainModel
import com.dennytech.resamopro.models.KeyValueModel

data class MainState(
    val error: String = "",
    val user: UserDomainModel? = null,
    val userStores: List<StoreDomainModel> = emptyList(),
    val currentStore: StoreDomainModel? = null,
    val productTypes: List<KeyValueModel> = emptyList(),
    val productCategories: List<KeyValueModel> = emptyList()
)