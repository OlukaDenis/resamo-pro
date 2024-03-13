package com.dennytech.data.remote.mapper

import com.dennytech.data.remote.models.StateRemoteModel
import com.dennytech.domain.models.StateDomainModel
import javax.inject.Inject

class StateRemoteMapper @Inject constructor(): BaseRemoteMapper<StateRemoteModel, StateDomainModel> {
    override fun toDomain(entity: StateRemoteModel): StateDomainModel {
        return StateDomainModel(
            name = entity.name.orEmpty(),
            abbreviation = entity.abbrv.orEmpty()
        )
    }
}