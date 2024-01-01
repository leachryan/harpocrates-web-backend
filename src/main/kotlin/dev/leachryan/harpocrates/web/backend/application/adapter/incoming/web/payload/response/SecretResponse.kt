package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response

import java.util.*

data class SecretResponse(
    val id: UUID,
    val value: String,
    val remainingViews: Int
)
