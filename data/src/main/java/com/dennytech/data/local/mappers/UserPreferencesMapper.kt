package com.dennytech.data.local.mappers

import com.dennytech.data.UserPreferences
import com.dennytech.domain.models.UserDomainModel
import javax.inject.Inject

class UserPreferencesMapper @Inject constructor() {
    fun toDomain(entity: UserPreferences) : UserDomainModel {
        return UserDomainModel(
            id = entity.id,
            phone = entity.phone,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            role = entity.role,
            status = entity.status,
            fullName = "${entity.firstName} ${entity.lastName}",
            lastLogin = ""
        )
    }
}