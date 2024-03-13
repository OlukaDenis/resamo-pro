package com.dennytech.domain.utils

import java.security.SecureRandom

object DomainHelpers {
    fun getRandomNumber(): Long {
        val min = 100L
        val max = 1000000L
        val random = SecureRandom()
        return random.nextLong().rem(max - min + 1) + min
    }
}