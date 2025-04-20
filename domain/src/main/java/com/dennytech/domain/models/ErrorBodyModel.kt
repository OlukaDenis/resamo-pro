package com.dennytech.domain.models

data class ErrorBody(
    var errorModel: ErrorModel? = null,
    var status: String? = "",
    var message: String? = ""
)

data class ErrorModel(
    val code: Int
)
