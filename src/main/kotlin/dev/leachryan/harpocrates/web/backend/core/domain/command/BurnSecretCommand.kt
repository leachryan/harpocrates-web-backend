package dev.leachryan.harpocrates.web.backend.core.domain.command

import java.util.UUID

data class BurnSecretCommand(
    val id: UUID
)
