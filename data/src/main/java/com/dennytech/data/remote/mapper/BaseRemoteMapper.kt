package com.dennytech.data.remote.mapper

interface BaseRemoteMapper<REMOTE, DOMAIN> {
    fun toDomain(entity: REMOTE): DOMAIN
}