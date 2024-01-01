package dev.leachryan.harpocrates.web.backend.core.domain.handler

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import dev.leachryan.harpocrates.web.backend.core.domain.command.BurnSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.command.CreateSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec.AESCodec
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class SecretCommandHandlerTest {

    private val secretPort: SecretPort = mockk()

    private val secretCommandHandler = SecretCommandHandler(secretPort)

    private val message = "secret"

    private val secretKey = "super-secret-key"

    private val initVector = "super-secret-key"

    private val encryptedValue = AESCodec.encrypt(
        message = message,
        secretKey = secretKey,
        initVector =  initVector
    )

    private val secret = Secret(
        id = UUID.randomUUID(),
        value = encryptedValue,
        remainingViews = 10
    )

    @Test
    fun `burn secret`() {
        val command = BurnSecretCommand(secret.id)

        justRun {
            secretPort.deleteSecret(secret.id)
        }

        assertThat(secretCommandHandler.burnSecret(command)).isInstanceOf(Unit::class.java)

        verify {
            secretPort.deleteSecret(secret.id)
        }
    }

    @Test
    fun `create secret`() {
        val command = CreateSecretCommand(
            value = message,
            maxViews = secret.remainingViews,
            secretKey = secretKey,
            initVector = initVector
        )

        every {
            secretPort.persistSecret(any())
        } returns secret

        val createdSecret = secretCommandHandler.createSecret(command)

        assertThat(createdSecret.id).isEqualTo(secret.id)
        assertThat(createdSecret.value).isEqualTo(encryptedValue)
        assertThat(createdSecret.remainingViews).isEqualTo(secret.remainingViews)

        verify {
            secretPort.persistSecret(any())
        }
    }
}