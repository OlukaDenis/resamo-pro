package com.dennytech.data.local.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dennytech.data.TokenPreferences
import java.io.InputStream
import java.io.OutputStream

object TokenPreferenceSerializer : Serializer<TokenPreferences> {
    override val defaultValue: TokenPreferences = TokenPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenPreferences = try {
        TokenPreferences.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: TokenPreferences, output: OutputStream) = t.writeTo(output)
}