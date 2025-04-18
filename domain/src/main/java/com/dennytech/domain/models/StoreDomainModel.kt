package com.dennytech.domain.models

data class StoreDomainModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: Int = -1,
    val location: String = "",
    val userId: String = "",
    val createdBy: String = "",
    val users: List<StoreUserDomainModel> = emptyList(),
    val categories: List<ProductCategoryDomainModel> = emptyList(),
    val brands: List<String> = emptyList(),
    val productTypes: List<String> = emptyList()
)