package com.dennytech.data.remote.models

data class CountryRemoteModel(
    val ISO2: String?,
    val display: String?,
    val currency: ArrayList<String>?,
    val dialCode: String?
)

data class CountryResponseModel(
    val data: List<CountryRemoteModel>
)