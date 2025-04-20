package com.dennytech.data.remote.models


data class RemoteRevenueResponse(
    val data: RemoteRevenueModel
)

data class RemoteRevenueModel(
    val revenue: Long?
)