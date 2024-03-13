package com.dennytech.data.remote.mapper

import com.dennytech.data.remote.models.TokenRemoteModel
import com.dennytech.domain.models.TokenDomainModel
import javax.inject.Inject

class RemoteTokenMapper @Inject constructor() :
    BaseRemoteMapper<TokenRemoteModel, TokenDomainModel> {
    override fun toDomain(entity: TokenRemoteModel): TokenDomainModel {
        return TokenDomainModel(
            token = entity.token.orEmpty(),
            unread = entity.unread?.toLong() ?: 0L,
            expiresIn = 0L
        )
    }
}