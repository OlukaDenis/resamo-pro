package com.dennytech.data.remote.models

data class ProvinceRemoteModel(
    val display: String?,
    val abbrv: String?
)

data class ProvinceResponseModel(
    val data: List<ProvinceRemoteModel>
)