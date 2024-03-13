package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.RemoteAddressModel.Companion.toDomain
import com.dennytech.domain.models.PaymentMethodDomainModel

data class RemotePaymentMethodModel(
    val _id: String,
    val accountNumber: String?,
    val bankCode: String?,
    val createdAt: String?,
    val billingAddress: RemoteAddressModel?,
    val currency: String?,
    val  account_bank: String?,
    val account_number: String?,
    val type: String?,
    val updatedAt: String?
) {
    companion object {
        fun RemotePaymentMethodModel.toDomain(profileId: String, type: String): PaymentMethodDomainModel {
            return PaymentMethodDomainModel(
                id = this._id,
                profileId = profileId,
                userId = profileId,
                accountNumber = this.accountNumber ?: this.account_number ?: "",
                accountBank = this.account_bank.orEmpty(),
                bankCode = this.bankCode.orEmpty(),
                createdAt = this.createdAt.orEmpty(),
                billingAddress = this.billingAddress?.toDomain(profileId),
                type = type,
                currency = this.currency.orEmpty(),
                updatedAt = this.updatedAt.orEmpty()
            )
        }
    }
}