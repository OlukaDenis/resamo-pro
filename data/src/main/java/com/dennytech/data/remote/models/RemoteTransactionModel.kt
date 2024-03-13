package com.dennytech.data.remote.models

import com.dennytech.data.remote.models.UserRemoteModel.Companion.toDomain
import com.dennytech.domain.models.FeeQuoteDomainModel
import com.dennytech.domain.models.TransactionDomainModel

data class TransactionsResponseModel(
    val data: List<RemoteTransactionModel>
)
data class RemoteTransactionModel(
    val _id: String,
    val type: String?,
    val amount: Double?,
    val status: String?,
    val currency: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val declinedAt: String?,
    val receiver: UserRemoteModel?,
    val sender: UserRemoteModel?,
    val details: RemoteDetailsModel?
) {

    companion object {

        fun RemoteTransactionModel.toDomain() : TransactionDomainModel {
            return TransactionDomainModel(
                id = this._id,
                type = this.type.orEmpty(),
                amount = this.amount ?: 0.0,
                status = this.status.orEmpty(),
                createdAt = this.createdAt.orEmpty(),
                updatedAt = this.updatedAt.orEmpty(),
                declinedAt = this.declinedAt.orEmpty(),
                receiver = this.receiver?.toDomain(),
                sender = this.sender?.toDomain(),
                currency = this.currency.orEmpty(),
                feeQuote = this.details?.feeQuote?.let {
                    FeeQuoteDomainModel(
                        sourceAmount = it.data.amount ?: 0.0,
                        destinationAmount = it.quoted.destinationAmount ?: 0.0,
                        rate = it.data.rate ?: 0.0,
                        destinationCurrency = it.data.destinationCurrency.orEmpty(),
                        sourceCurrency = it.data.sourceCurrency.orEmpty()
                    )
                }
            )
        }
    }
}

data class RemoteDetailsModel(
    val feeQuote: RemoteFeeQuotedModel?
)

data class RemoteFeeQuotedModel(
    val data: FeeDataModel,
    val quoted: FeeQuotedModel
)

data class FeeDataModel(
    val sourceCurrency: String?,
    val destinationCurrency: String?,
    val rate: Double?,
    val amount: Double?
)
data class FeeQuotedModel(
    val rate: Double?,
    val destinationAmount: Double?
)