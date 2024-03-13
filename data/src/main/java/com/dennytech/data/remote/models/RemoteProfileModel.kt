package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteAddressModel.Companion.toDomain
import com.dennytech.data.remote.models.RemotePaymentMethodModel.Companion.toDomain
import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.domain.models.ProfileDomainModel

data class RemoteProfileModel(
    val _id: String,
    val mobileCountryCode: String?,
    val mobileNumber: String?,
    val name: String?,
    val address: RemoteAddressModel?,
    val paymentMethods: List<RemotePaymentMethodModel>?,
    val linked: UserRemoteModel?
) {

    companion object {
        fun RemoteProfileModel.toDomain(userId: String, type: String): ProfileDomainModel {
            return ProfileDomainModel(
                id = this._id,
                userId = userId,
                address = this.address?.toDomain(this._id),
                linked = this.linked?.toDomain(),
                mobileCountryCode = this.mobileCountryCode.orEmpty(),
                mobileNumber = this.mobileNumber.orEmpty(),
                name = this.name.orEmpty(),
                role = type,
                trulipayUser = linked != null,
                paymentMethods = this.paymentMethods?.let { list-> list.map { it.toDomain(this._id, "profile") } } ?: emptyList(),
            )
        }
    }
}