package dev.leachryan.harpocrates.web.backend.core.domain.handler

import dev.leachryan.harpocrates.web.backend.core.domain.command.BurnSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.command.CreateSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec.AESCodec
import dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec.BCryptCodec
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.port.incoming.BurnSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.incoming.CreateSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort
import java.util.*

class SecretCommandHandler(
    private val secretPort: SecretPort
) : CreateSecretUseCase, BurnSecretUseCase {
    override fun burnSecret(command: BurnSecretCommand) = secretPort.deleteSecret(command.id)

    override fun createSecret(command: CreateSecretCommand): Secret {
        val encryptedValue = AESCodec.encrypt(
            message = command.value,
            secretKey = command.secretKey,
            initVector = command.initVector
        )

        val password = command.password?.let {
            BCryptCodec.hash(it)
        }

        val secret = Secret(
            id = UUID.randomUUID(),
            value = encryptedValue,
            remainingViews = command.maxViews,
            password = password
        )

        return secretPort.persistSecret(secret)
    }
}