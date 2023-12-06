package dev.leachryan.harpocrates.web.backend.core.domain.model

import java.util.UUID

data class Secret(
    // The unique identifier
    val id: UUID,

    // The encoded string value of the secret
    val value: String,

    // The maximum number of times the secret can be accessed
    val accessLimit: Int,

    // An optional hashed password to be verified before accessing the secret
    val password: String? = null,
)
