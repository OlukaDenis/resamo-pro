package com.dennytech.domain.repository

import com.dennytech.domain.models.ErrorBody

interface UtilRepository {
    fun getNetworkError(throwable: Throwable): String
    fun getErrorBody(throwable: Throwable): ErrorBody
    fun currentDate(): String
}