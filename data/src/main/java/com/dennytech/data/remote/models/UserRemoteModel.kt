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
    val _id: String,
    val name: String?,
    val username: String?,
    val email: String?,
    val mobileCountryCode: String?,
    val mobileNumber: String?,
    val faceIdEnabled: Boolean?,
    val pinSetup: Boolean?,
    val paymentMethods: List<RemotePaymentMethodModel>?,
    val receivers: List<RemoteProfileModel>?,
    val senders: List<RemoteProfileModel>?,
    val accountBalance: Double?,
    val address: RemoteAddressModel?,
    val dob: String?,
    val currency: String?,
    val kyc: RemoteKycModel?,
    val profileImage: RemoteProfileImageModel?
) {

    companion object {
        fun UserRemoteModel.toDomain(): UserDomainModel {
            return UserDomainModel(
                id = this._id,
                name = this.name.orEmpty(),
                username = this.username.orEmpty(),
                email = this.email.orEmpty(),
                mobileCountryCode = this.mobileCountryCode.orEmpty(),
                mobileNumber = this.mobileNumber.orEmpty(),
                faceIdEnabled = this.faceIdEnabled ?: false,
                address = this.address?.toDomain(this._id),
                dob = this.dob.orEmpty(),
                accountBalance = this.accountBalance ?: 0.0,
                pinSetup = this.pinSetup ?: false,
                currency =  this.currency.orEmpty(),
                kycStatus = this.kyc?.status ?: "",
                receivers = this.receivers?.let { list -> list.map { it.toDomain(this._id, "receiver") } } ?: emptyList(),
                senders = this.senders?.let { list -> list.map { it.toDomain(this._id, "sender") } } ?: emptyList(),
                paymentMethods = this.paymentMethods?.let { list -> list.map { it.toDomain(this._id,"user") } } ?: emptyList(),
                profileImage = this.profileImage?.let { model ->
                    model.sizes?.let { if (it.isNotEmpty()) it[1].url!! else "" }
                } ?: ""
            )
        }
    }
}