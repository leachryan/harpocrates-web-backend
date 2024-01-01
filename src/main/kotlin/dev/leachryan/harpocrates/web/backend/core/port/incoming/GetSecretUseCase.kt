package dev.leachryan.harpocrates.web.backend.core.port.incoming

import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.domain.query.GetSecretQuery
import java.util.UUID

fun interface GetSecretUseCase {
    fun getSecret(query: GetSecretQuery): Secret?
}