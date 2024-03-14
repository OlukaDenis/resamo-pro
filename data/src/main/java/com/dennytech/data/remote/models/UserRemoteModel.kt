package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteAddressModel.Companion.toDomain
import com.dennytech.data.remote.models.RemotePaymentMethodModel.Companion.toDomain
import com.dennytech.data.remote.models.RemoteProfileModel.Companion.toDomain
import com.dennytech.domain.models.UserDomainModel

data class CheckUsernameResponse(
    val data: Boolean?
)

data class SignupResponseModel(
    val data: Any?
)

data class RemoteKycModel(
    val status: String?
)

data class UserResponseModel(
    val data: UserRemoteModel
)

data class UserRemoteModel(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val token: String?,
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
                token = this.token.orEmpty(),
                role = this.role ?: 0,
                fullName = "$firstName $lastName"
            )
        }
    }
}