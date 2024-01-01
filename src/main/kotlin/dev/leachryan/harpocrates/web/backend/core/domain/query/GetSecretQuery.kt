package dev.leachryan.harpocrates.web.backend.core.domain.query

import java.util.*

data class GetSecretQuery(
    val id: UUID,
    val secretKey: String,
    val initVector: String
)
