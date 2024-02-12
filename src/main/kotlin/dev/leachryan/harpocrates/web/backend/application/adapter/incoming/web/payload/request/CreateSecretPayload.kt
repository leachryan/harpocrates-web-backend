package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request

data class CreateSecretPayload(
    val value: String,
    val password: String? = null,
    val maxViews: Int
)