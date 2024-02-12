package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request

import java.util.UUID

data class BurnSecretPayload(
    val id: UUID
)