package dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

class BCryptCodecTest {

    private val correctPassword = "password"

    private val incorrectPassword = "incorrect"

    @Test
    fun `test verify when passwords should match`() {
        val hashed = BCryptCodec.hash(correctPassword)

        assertThat(BCryptCodec.verify(correctPassword, hashed)).isTrue()
    }

    @Test
    fun `test verify when passwords should not match`() {
        val hashed = BCryptCodec.hash(correctPassword)

        assertThat(BCryptCodec.verify(incorrectPassword, hashed)).isFalse()
    }

    @Test
    fun `test hash`() {
        val hashed = BCryptCodec.hash(correctPassword)

        assertThat(hashed).isInstanceOf(String::class)
        assertThat(hashed).isNotEqualTo(correctPassword)
    }
}