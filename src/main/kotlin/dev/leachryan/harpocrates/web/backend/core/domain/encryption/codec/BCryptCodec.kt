package dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptCodec {

    fun hash(value: String): String {
        return BCrypt.withDefaults().hashToString(12, value.toCharArray())
    }

    fun verify(rawValue: String, hashedValue: String): Boolean {
        val result = BCrypt.verifyer().verify(rawValue.toCharArray(), hashedValue)
        return result.verified
    }
}