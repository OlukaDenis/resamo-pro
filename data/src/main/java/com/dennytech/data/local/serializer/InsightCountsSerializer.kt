package com.dennytech.data.local.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dennytech.data.InsightCounts
import java.io.InputStream
import java.io.OutputStream

object InsightCountsSerializer : Serializer<InsightCounts> {
    override val defaultValue: InsightCounts = InsightCounts.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): InsightCounts = try {
        InsightCounts.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: InsightCounts, output: OutputStream) = t.writeTo(output)
}