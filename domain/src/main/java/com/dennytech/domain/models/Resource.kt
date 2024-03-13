package com.dennytech.domain.models

sealed class Resource<out R> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: String) : Resource<Nothing>()
}

sealed class AppResource <out M> {
    data object Loading : AppResource<Nothing>()
    data class Success<out T>(val data: T) : AppResource<T>()
    data class Error(val exception: ErrorBody) : AppResource<Nothing>()
}
