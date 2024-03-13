package com.dennytech.data.impl

import com.dennytech.data.utils.extractErrorBody
import com.dennytech.data.utils.resolveError
import com.dennytech.domain.models.ErrorBody
import com.dennytech.domain.repository.UtilRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UtilRepositoryImpl @Inject constructor(): UtilRepository {
    override fun getNetworkError(throwable: Throwable): String = throwable.resolveError()
    override fun getErrorBody(throwable: Throwable): ErrorBody = throwable.extractErrorBody()

    override fun currentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
            .format(Date())
    }
}