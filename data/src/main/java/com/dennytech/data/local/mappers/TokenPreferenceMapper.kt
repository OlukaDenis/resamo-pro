package com.dennytech.data.local.mappers

import com.dennytech.data.TokenPreferences
import com.dennytech.domain.models.TokenDomainModel
import javax.inject.Inject

class TokenPreferenceMapper @Inject constructor() {
    fun toDomain(entity: TokenPreferences) : TokenDomainModel {
        return TokenDomainModel(
            token = entity.token,
            expiresIn = entity.expiresIn,
            unread = entity.unread
        )
    }
}