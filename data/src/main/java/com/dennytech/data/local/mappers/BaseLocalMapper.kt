package com.dennytech.data.local.mappers

interface BaseLocalMapper<LOCAL, DOMAIN> {
    fun toDomain(entity: LOCAL): DOMAIN
    fun toLocal(entity: DOMAIN): LOCAL
}