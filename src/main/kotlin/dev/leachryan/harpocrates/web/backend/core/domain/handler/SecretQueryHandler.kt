package dev.leachryan.harpocrates.web.backend.core.domain.handler

import dev.leachryan.harpocrates.web.backend.core.domain.encryption.codec.AESCodec
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.domain.query.GetSecretQuery
import dev.leachryan.harpocrates.web.backend.core.port.incoming.GetSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort

class SecretQueryHandler(
    private val secretPort: SecretPort
) : GetSecretUseCase {
    override fun getSecret(query: GetSecretQuery): Secret? {
        val foundSecret = secretPort.fetchSecret(query.id)

        val result = foundSecret?.let { secret ->

            // Decrypt the secret value
            val decryptedValue = AESCodec.decrypt(
                encryptedMessage = secret.value,
                secretKey = query.secretKey,
                initVector = query.initVector
            )

            // Determine the remaining views
            val newRemainingViews = secret.remainingViews - 1

            when (newRemainingViews > 0) {
                true -> {
                    // Update the secret with the new remaining views
                    secretPort.persistSecret(secret.copy(remainingViews = newRemainingViews))
                }
                else -> {
                    // Burn the secret, but return the value - the last view
                    secretPort.deleteSecret(secret.id)
                }
            }

            secret.copy(value = decryptedValue, remainingViews = newRemainingViews)
        }

        return result
    }
}