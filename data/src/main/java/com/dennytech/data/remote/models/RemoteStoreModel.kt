package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteCategoryModel.Companion.toDomain
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomainStoreUser
import com.dennytech.domain.models.StoreDomainModel

data class StoreResponseModel(
    val data: RemoteStoreModel
)

data class RemoteStoreModel(
    val id: String,
    val name: String?,
    val description: String?,
    val status: Int?,
    val location: String?,
    val createdBy: String?,
    val users: List<UserRemoteModel>?,
    val categories: List<RemoteCategoryModel>?,
    val brands: List<String>?,
    val productTypes: List<String>?
) {
    companion object {
        fun RemoteStoreModel.toDomain(userId: String): StoreDomainModel {
            return StoreDomainModel(
                id = this.id,
                name = this.name.orEmpty(),
                description = this.description.orEmpty(),
                status = this.status ?: 0,
                location = this.location.orEmpty(),
                createdBy = this.createdBy.orEmpty(),
                userId = userId,
                users = this.users?.map { it.toDomainStoreUser(this.id) } ?: emptyList(),
                categories = this.categories?.map { it.toDomain(this.id) } ?: emptyList(),
                brands = this.brands.orEmpty(),
                productTypes = this.productTypes.orEmpty()
            )
        }
    }
}