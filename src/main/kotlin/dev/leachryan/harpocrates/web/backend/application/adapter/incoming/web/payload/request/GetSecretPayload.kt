package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request

import java.util.UUID

data class GetSecretPayload(
    val id: UUID,
    val password: String? = null
)
