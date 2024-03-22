package com.dennytech.data.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.dennytech.domain.models.ErrorBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val MAX_PAGE_SIZE = 10

fun Throwable.resolveError(): String {

    return when (this) {
        is SocketTimeoutException -> {
            "Request timeout, Please try again"
        }
        is ConnectException -> {
            "No internet access"
        }
        is UnknownHostException -> {
            "No internet access"
        }

        is IOException -> {
            "No internet access"
        }

        is HttpException -> {
            try {
                val responseBody = this.response()?.errorBody() ?: throw  Exception()

                val json =  JsonParser.parseString(responseBody.string())

                parseHTTPError(json)
            } catch (ex: Exception) {
                Timber.e(ex)
                "Connection failed"
            }
        }

        else -> {
            "Error occurred"
        }
    }
}

fun Throwable.extractErrorBody(): ErrorBody {
    var err = ErrorBody()

    when (this) {
        is SocketTimeoutException -> {
            err.message = "Request timeout, Please try again"
        }
        is ConnectException -> {
            err.message = "No internet access"
        }
        is UnknownHostException -> {
            err.message =  "No internet access"
        }

        is IOException -> {
            err.message = "No internet access"
        }

        is HttpException -> {
            try {
                val responseBody = this.response()?.errorBody() ?: throw  Exception()

                val json =  Gson().fromJson(responseBody.string(), ErrorBody::class.java)
                Timber.d("ErrorBody: %s", json)
                err = json
            } catch (ex: Exception) {
                Timber.e(ex)
                err.message =  "Connection failed"
            }
        }

        else -> {
            err.message = "Error occurred"
        }
    }

    return err
}

fun parseHTTPError(errorEl: JsonElement): String {

    if (errorEl.isJsonArray) return "Error occurred"

    val json =  Gson().fromJson(errorEl, Map::class.java)

    return try {
        val message = json["message"]
        message.toString()
    } catch (e: Exception) {
        "Error occurred"
    }
}

fun ContentResolver.readAsRequestBody(uri: Uri): RequestBody =
    object: RequestBody() {
        override fun contentType(): MediaType? =
            this@readAsRequestBody.getType(uri)?.toMediaTypeOrNull()

        override fun writeTo(sink: BufferedSink) {
            this@readAsRequestBody.openInputStream(uri)?.source()?.use(sink::writeAll)
        }

        override fun contentLength(): Long =
            this@readAsRequestBody.query(uri, null, null, null, null)?.use { cursor ->
                val sizeColumnIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                cursor.getLong(sizeColumnIndex)
            } ?: super.contentLength()
    }

fun Long.isAccessTokenExpired(): Boolean {
    val currentTimeMillis = System.currentTimeMillis()
    val expiry = this * 1000

    return currentTimeMillis >= expiry
}

