package com.dennytech.data.local.mappers

import com.dennytech.data.local.models.StateEntity
import com.dennytech.domain.models.StateDomainModel
import javax.inject.Inject

class StateEntityMapper @Inject constructor() : BaseLocalMapper<StateEntity, StateDomainModel> {
    override fun toDomain(entity: StateEntity): StateDomainModel {
        return StateDomainModel(name = entity.name, abbreviation = entity.abbreviation)
    }

    override fun toLocal(entity: StateDomainModel): StateEntity {
        return StateEntity(id = 0L, name = entity.name, abbreviation = entity.abbreviation)
    }
}