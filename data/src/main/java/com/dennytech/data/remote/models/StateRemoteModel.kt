package com.dennytech.data.remote.models

data class StateRemoteModel(
    val name: String?,
    val abbrv: String?
)

data class StateResponseModel(
    val data: List<StateRemoteModel>
)