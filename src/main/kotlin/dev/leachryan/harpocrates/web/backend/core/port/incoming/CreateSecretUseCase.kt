package dev.leachryan.harpocrates.web.backend.core.port.incoming

import dev.leachryan.harpocrates.web.backend.core.domain.command.CreateSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret

fun interface CreateSecretUseCase {
    fun createSecret(command: CreateSecretCommand): Secret
}