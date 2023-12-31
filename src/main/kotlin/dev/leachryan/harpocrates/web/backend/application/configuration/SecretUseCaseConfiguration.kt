package dev.leachryan.harpocrates.web.backend.application.configuration

import dev.leachryan.harpocrates.web.backend.core.domain.handler.SecretCommandHandler
import dev.leachryan.harpocrates.web.backend.core.domain.handler.SecretQueryHandler
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecretUseCaseConfiguration {

    @Bean
    fun secretCommandHandler(secretPort: SecretPort): SecretCommandHandler {
        return SecretCommandHandler(secretPort)
    }

    @Bean
    fun secretQueryHandler(secretPort: SecretPort): SecretQueryHandler {
        return SecretQueryHandler(secretPort)
    }
}