package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request

data class CreateSecretRequest(
    val value: String,
    val maxViews: Int
)