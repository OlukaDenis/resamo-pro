package com.dennytech.data.remote.models

import com.dennytech.domain.models.StoreDomainModel

data class StoreResponseModel(
    val data: RemoteStoreModel
)

data class RemoteStoreModel(
    val id: String?,
    val name: String?,
    val description: String?,
    val status: Int?,
    val location: String?,
    val createdBy: String?
) {
    companion object {
        fun RemoteStoreModel.toDomain(userId: String): StoreDomainModel {
            return StoreDomainModel(
                id = this.id.orEmpty(),
                name = this.name.orEmpty(),
                description = this.description.orEmpty(),
                status = this.status ?: 0,
                location = this.location.orEmpty(),
                createdBy = this.createdBy.orEmpty(),
                userId = userId
            )
        }
    }
}