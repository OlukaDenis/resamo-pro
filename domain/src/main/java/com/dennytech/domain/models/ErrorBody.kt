package com.dennytech.domain.models

data class ErrorBody(
    var error: Error? = null,
    var status: String? = "",
    var message: String? = "",
    var errors: List<FieldError>? = emptyList()
)

data class Error(
    val code: Int
)

data class FieldError(
    val msg: String,
    val param: String
)
