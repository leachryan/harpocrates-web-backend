package dev.leachryan.harpocrates.web.backend.application.adapter.outgoing.persistence

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import java.util.*

class SecretRepositoryTest {

    private val redisTemplate: RedisTemplate<UUID, Secret> = mockk()

    private val secretRepository = SecretRepository(redisTemplate)

    private val secret = Secret(
        id = UUID.randomUUID(),
        value = "secret",
        remainingViews = 10
    )

    @Test
    fun `persist secret`() {
        justRun {
            redisTemplate.opsForValue().set(secret.id, secret)
        }

        val persistedSecret = secretRepository.persistSecret(secret)

        assertThat(persistedSecret.id).isEqualTo(secret.id)
        assertThat(persistedSecret.value).isEqualTo(secret.value)
        assertThat(persistedSecret.remainingViews).isEqualTo(secret.remainingViews)

        verify {
            redisTemplate.opsForValue().set(secret.id, secret)
        }
    }

    @Test
    fun `fetch secret`() {
        every {
            redisTemplate.opsForValue().get(secret.id)
        } returns secret

        val fetchedSecret = secretRepository.fetchSecret(secret.id)

        assertThat(fetchedSecret).isNotNull()

        fetchedSecret!!.run {
            assertThat(id).isEqualTo(secret.id)
            assertThat(value).isEqualTo(secret.value)
            assertThat(remainingViews).isEqualTo(secret.remainingViews)
        }

        verify {
            redisTemplate.opsForValue().get(secret.id)
        }
    }

    @Test
    fun `delete secret`() {
        justRun {
            redisTemplate.opsForValue().getAndDelete(secret.id)
        }

        val deletedSecret = secretRepository.deleteSecret(secret.id)

        assertThat(deletedSecret).isInstanceOf(Unit::class.java)

        verify {
            redisTemplate.opsForValue().getAndDelete(secret.id)
        }
    }
}