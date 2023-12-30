package dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCodec {

    private const val ALGORITHM = "AES"

    private val CHARSET = Charsets.UTF_8

    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    /**
     * The secretKey and initVector must be of valid sizes
     */

    fun encrypt(message: String, secretKey: String, initVector: String): String {
        val ivSpec = createIvSpec(initVector)

        val secretKeySpec = createSecretKeySpec(secretKey)

        val cipher = createCipher()
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

        val encryptedBytes = cipher.doFinal(message.toByteArray())

        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedMessage: String, secretKey: String, initVector: String): String {
        val ivSpec = createIvSpec(initVector)

        val secretKeySpec = createSecretKeySpec(secretKey)

        val cipher = createCipher()
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))

        return String(decryptedBytes)
    }

    private fun createIvSpec(initVector: String): IvParameterSpec =
        IvParameterSpec(initVector.toByteArray(CHARSET))

    private fun createSecretKeySpec(secretKey: String): SecretKeySpec =
        SecretKeySpec(secretKey.toByteArray(CHARSET), ALGORITHM)

    private fun createCipher(): Cipher = Cipher.getInstance(TRANSFORMATION)
}