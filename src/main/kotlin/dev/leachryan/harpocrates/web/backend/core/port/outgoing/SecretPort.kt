package dev.leachryan.harpocrates.web.backend.core.port.outgoing

import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import java.util.UUID

interface SecretPort {
    fun persistSecret(secret: Secret): Secret
    fun fetchSecret(id: UUID): Secret?
    fun deleteSecret(id: UUID)
}