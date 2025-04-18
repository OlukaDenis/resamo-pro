package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteStoreModel.Companion.toDomain
import com.dennytech.domain.models.StoreUserDomainModel
import com.dennytech.domain.models.UserDomainModel

data class GenericUserResponse(
    val data: Any?
)

data class SignupResponseModel(
    val data: Any?
)

data class UserListResponse(
    val data: List<UserRemoteModel>
)

data class UserResponse(
    val data: UserRemoteModel
)

data class UserRemoteModel(
    val id: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val token: String? = null,
    val expiresIn: Long? = null,
    val status: Int? = null,
    val lastLogin: String? = null,
    val phone: String? = null,
    val role: Int? = null,
    val stores: List<RemoteStoreModel>? = null,
    val defaultStore: String? = null
) {

    companion object {
        fun UserRemoteModel.toDomainUser(): UserDomainModel {
            return UserDomainModel(
                id = this.id,
                firstName = this.firstName.orEmpty(),
                lastName = this.lastName.orEmpty(),
                phone = this.phone.orEmpty(),
                email = this.email.orEmpty(),
                role = this.role ?: -1,
                status = this.status ?: 0,
                fullName = "$firstName $lastName",
                lastLogin = this.lastLogin.orEmpty(),
                stores = stores?.map { it.toDomain(this.id) }.orEmpty(),
                defaultStore = defaultStore.orEmpty()
            )
        }

        fun UserRemoteModel.toDomainStoreUser(storeId: String): StoreUserDomainModel {
            return StoreUserDomainModel(
                id = this.id,
                firstName = this.firstName.orEmpty(),
                lastName = this.lastName.orEmpty(),
                phone = this.phone.orEmpty(),
                email = this.email.orEmpty(),
                role = this.role ?: -1,
                status = this.status ?: 0,
                fullName = "$firstName $lastName",
                lastLogin = this.lastLogin.orEmpty(),
                stores = stores?.map { it.toDomain(this.id) }.orEmpty(),
                defaultStore = defaultStore.orEmpty(),
                storeId = storeId
            )
        }
    }
}