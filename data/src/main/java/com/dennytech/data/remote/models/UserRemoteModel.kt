package com.dennytech.data.remote.models

import com.dennytech.domain.models.UserDomainModel

data class CheckUsernameResponse(
    val data: Boolean?
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
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val token: String?,
    val expiresIn: Long?,
    val status: Int?,
    val phone: String?,
    val role: Int?,
) {

    companion object {
        fun UserRemoteModel.toDomain(): UserDomainModel {
            return UserDomainModel(
                id = this.id,
                firstName = this.firstName.orEmpty(),
                lastName = this.lastName.orEmpty(),
                phone = this.phone.orEmpty(),
                email = this.email.orEmpty(),
                role = this.role ?: 0,
                status = this.status ?: 0,
                fullName = "$firstName $lastName"
            )
        }
    }
}