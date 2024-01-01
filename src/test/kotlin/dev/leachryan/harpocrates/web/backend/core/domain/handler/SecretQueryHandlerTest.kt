package dev.leachryan.harpocrates.web.backend.core.domain.handler

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec.AESCodec
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.domain.query.GetSecretQuery
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class SecretQueryHandlerTest {

    private val secretPort: SecretPort = mockk()

    private val secretQueryHandler = SecretQueryHandler(secretPort)

    private val message = "secret"

    private val secretKey = "super-secret-key"

    private val initVector = "super-secret-key"

    private val encryptedValue = AESCodec.encrypt(
        message = message,
        secretKey = secretKey,
        initVector = initVector
    )

    private val secret = Secret(
        id = UUID.randomUUID(),
        value = encryptedValue,
        remainingViews = 10
    )

    @Test
    fun `get secret when secret not found`() {
        val query = GetSecretQuery(
            id = secret.id,
            secretKey = secretKey,
            initVector = initVector
        )

        every {
            secretPort.fetchSecret(secret.id)
        } returns null

        val foundSecret = secretQueryHandler.getSecret(query)

        assertThat(foundSecret).isNull()

        verify {
            secretPort.fetchSecret(secret.id)
        }
    }

    @Test
    fun `get secret when secret is found and has available views`() {
        val query = GetSecretQuery(
            id = secret.id,
            secretKey = secretKey,
            initVector = initVector
        )

        every {
            secretPort.fetchSecret(secret.id)
        } returns secret

        every {
            secretPort.persistSecret(secret.copy(
                remainingViews = secret.remainingViews - 1
            ))
        } returns secret.copy(remainingViews = secret.remainingViews - 1)

        val foundSecret = secretQueryHandler.getSecret(query)

        assertThat(foundSecret).isNotNull()

        foundSecret!!.run {
            assertThat(id).isEqualTo(secret.id)
            assertThat(value).isEqualTo(message)
            assertThat(remainingViews).isEqualTo(secret.remainingViews - 1)
        }

        verify {
            secretPort.fetchSecret(secret.id)
        }

        verify {
            secretPort.persistSecret(secret.copy(
                remainingViews = secret.remainingViews - 1
            ))
        }
    }

    @Test
    fun `get secret when secret is found and has no more views`() {
        val query = GetSecretQuery(
            id = secret.id,
            secretKey = secretKey,
            initVector = initVector
        )

        every {
            secretPort.fetchSecret(secret.id)
        } returns secret.copy(remainingViews = 1)

        justRun {
            secretPort.deleteSecret(secret.id)
        }

        val foundSecret = secretQueryHandler.getSecret(query)

        assertThat(foundSecret).isNotNull()

        foundSecret!!.run {
            assertThat(id).isEqualTo(secret.id)
            assertThat(value).isEqualTo(message)
            assertThat(remainingViews).isEqualTo(0)
        }

        verify {
            secretPort.fetchSecret(secret.id)
        }

        justRun {
            secretPort.deleteSecret(secret.id)
        }
    }
}