package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.PaymentMethodEntity
import com.dennytech.domain.models.PaymentMethodDomainModel
import javax.inject.Inject

class PaymentMethodEntityMapper @Inject constructor(): BaseLocalMapper<PaymentMethodEntity, PaymentMethodDomainModel> {
    override fun toDomain(entity: PaymentMethodEntity): PaymentMethodDomainModel {
        return PaymentMethodDomainModel(
            id = entity.id,
            profileId = entity.profileId,
            userId = entity.userId,
            accountNumber = entity.accountNumber,
            accountBank = entity.accountBank,
            bankCode = entity.bankCode,
            createdAt = entity.createdAt,
            type = entity.type,
            currency = entity.currency,
            updatedAt = entity.updatedAt,
            billingAddress = null
        )
    }

    override fun toLocal(entity: PaymentMethodDomainModel): PaymentMethodEntity {
        return PaymentMethodEntity(
            id = entity.id,
            profileId = entity.profileId,
            userId = entity.userId,
            accountNumber = entity.accountNumber,
            accountBank = entity.accountBank,
            bankCode = entity.bankCode,
            createdAt = entity.createdAt,
            type = entity.type,
            currency = entity.currency,
            updatedAt = entity.updatedAt,
        )
    }
}