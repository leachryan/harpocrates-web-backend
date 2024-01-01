package dev.leachryan.harpocrates.web.backend.application.adapter.outgoing.persistence

import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.port.outgoing.SecretPort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SecretRepository(
    private val redisTemplate: RedisTemplate<UUID, Secret>
) : SecretPort {
    override fun persistSecret(secret: Secret): Secret {
        redisTemplate.opsForValue().set(secret.id, secret)

        return secret
    }

    override fun fetchSecret(id: UUID): Secret? = redisTemplate.opsForValue().get(id)

    override fun deleteSecret(id: UUID) {
        redisTemplate.opsForValue().getAndDelete(id)
    }
}