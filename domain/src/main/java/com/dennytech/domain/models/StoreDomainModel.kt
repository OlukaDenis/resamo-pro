package com.dennytech.domain.models

data class StoreDomainModel(
    val id: String,
    val name: String,
    val description: String,
    val status: Int,
    val location: String,
    val userId: String,
    val createdBy: String,
    val users: List<StoreUserDomainModel>,
    val brands: List<String>,
)