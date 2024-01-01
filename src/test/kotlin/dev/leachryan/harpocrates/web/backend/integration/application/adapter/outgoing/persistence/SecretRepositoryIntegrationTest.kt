package dev.leachryan.harpocrates.web.backend.integration.application.adapter.outgoing.persistence

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import dev.leachryan.harpocrates.web.backend.application.adapter.outgoing.persistence.SecretRepository
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.integration.configuration.TestRedisConfiguration
import dev.leachryan.harpocrates.web.backend.integration.setup.IntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.util.*

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@ContextConfiguration(classes = [TestRedisConfiguration::class])
class SecretRepositoryIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var secretRepository: SecretRepository

    private val secret = Secret(
        id = UUID.randomUUID(),
        value = "secret",
        remainingViews = 10
    )

    @Test
    fun `persist secret`() {
        val persistedSecret = secretRepository.persistSecret(secret)

        assertThat(persistedSecret).isEqualTo(secret)
    }

    @Test
    fun `fetch secret`() {
        secretRepository.persistSecret(secret)

        val fetchedSecret = secretRepository.fetchSecret(secret.id)

        assertThat(fetchedSecret).isNotNull()

        fetchedSecret!!.run {
            assertThat(id).isEqualTo(secret.id)
            assertThat(value).isEqualTo(secret.value)
            assertThat(remainingViews).isEqualTo(secret.remainingViews)
        }
    }

    @Test
    fun `delete secret`() {
        secretRepository.persistSecret(secret)

        val deletedSecret = secretRepository.deleteSecret(secret.id)

        assertThat(deletedSecret).isInstanceOf(Unit::class.java)
    }
}