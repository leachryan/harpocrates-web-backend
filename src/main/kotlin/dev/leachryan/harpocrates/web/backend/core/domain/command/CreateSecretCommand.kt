package dev.leachryan.harpocrates.web.backend.core.domain.command

data class CreateSecretCommand(
    // The secret value that should be encrypted
    val value: String,
    val maxViews: Int,
    val secretKey: String,
    val initVector: String,
    val password: String?
)
