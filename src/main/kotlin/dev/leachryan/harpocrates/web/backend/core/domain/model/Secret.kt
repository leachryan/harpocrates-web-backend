package dev.leachryan.harpocrates.web.backend.core.domain.model

data class Secret(
    val value: String,
    val accessLimit: Int,
    val password: String? = null,
)
