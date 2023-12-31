package dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import kotlin.test.assertEquals

class AESCodecTest {

    private val secretKey = "0".repeat(16)

    private val initVector = "1".repeat(16)

    @Test
    fun `test encryption and decryption with a valid secret key and iv vector`() {
        val message = "Hello, world!"

        val encryptedMessage = AESCodec.encrypt(
            message = message,
            secretKey = secretKey,
            initVector = initVector
        )

        val decryptedMessage = AESCodec.decrypt(
            encryptedMessage = encryptedMessage,
            secretKey = secretKey,
            initVector = initVector
        )

        assertThat(decryptedMessage).isEqualTo(message)
    }

    @Test
    fun `test valid encryption and invalid decryption via secret key`() {
        val message = "Hello, world!"

        assertThrows<InvalidKeyException> {
            AESCodec.encrypt(
                message = message,
                secretKey = "0".repeat(15),
                initVector = initVector
            )
        }
    }

    @Test
    fun `test valid encryption and invalid decryption via init vector`() {
        val message = "Hello, world!"

        assertThrows<InvalidAlgorithmParameterException> {
            AESCodec.encrypt(
                message = message,
                secretKey = secretKey,
                initVector = "1".repeat(15)
            )
        }
    }
}