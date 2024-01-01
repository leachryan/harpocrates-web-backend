package dev.leachryan.harpocrates.web.backend.core.port.incoming

import dev.leachryan.harpocrates.web.backend.core.domain.command.BurnSecretCommand

fun interface BurnSecretUseCase {
    fun burnSecret(command: BurnSecretCommand)
}