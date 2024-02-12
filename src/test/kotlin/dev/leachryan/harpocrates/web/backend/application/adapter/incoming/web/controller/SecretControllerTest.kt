package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.BurnSecretPayload
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.CreateSecretPayload
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.GetSecretPayload
import dev.leachryan.harpocrates.web.backend.core.domain.command.BurnSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.command.CreateSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.model.Secret
import dev.leachryan.harpocrates.web.backend.core.domain.query.GetSecretQuery
import dev.leachryan.harpocrates.web.backend.core.port.incoming.BurnSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.incoming.CreateSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.incoming.GetSecretUseCase
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import java.net.URI
import java.util.UUID

class SecretControllerTest {

    private val createSecretUseCase: CreateSecretUseCase = mockk()
    private val getSecretUseCase: GetSecretUseCase = mockk()
    private val burnSecretUseCase: BurnSecretUseCase = mockk()
    private val environment: Environment = mockk()

    private val secretController = SecretController(
        createSecretUseCase = createSecretUseCase,
        getSecretUseCase = getSecretUseCase,
        burnSecretUseCase = burnSecretUseCase,
        environment = environment
    )

    private val secretKey = "secret-key"
    private val initVector = "init-vector"

    private val secret = Secret(
        id = UUID.randomUUID(),
        value = "message",
        remainingViews = 10
    )

    @Test
    fun `create secret without password`() {
        val request = CreateSecretPayload(
            value = "message",
            maxViews = 10,
            password = null
        )

        val command = CreateSecretCommand(
            value = "message",
            maxViews = 10,
            secretKey = secretKey,
            initVector = initVector,
            password = null
        )

        every {
            environment.getProperty("harpocrates.secret.key")
        } returns secretKey

        every {
            environment.getProperty("harpocrates.init.vector")
        } returns initVector

        every {
            createSecretUseCase.createSecret(command)
        } returns secret

        val response = secretController.createSecret(request)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.headers.location).isEqualTo(URI.create(secret.id.toString()))

        verify {
            environment.getProperty("harpocrates.secret.key")
        }

        verify {
            environment.getProperty("harpocrates.init.vector")
        }

        verify {
            createSecretUseCase.createSecret(command)
        }
    }

    @Test
    fun `get secret by id when found without password`() {
        val query = GetSecretQuery(
            id = secret.id,
            secretKey = secretKey,
            initVector = initVector,
            password = null
        )

        val body = GetSecretPayload(id = secret.id, password = null)

        every {
            environment.getProperty("harpocrates.secret.key")
        } returns secretKey

        every {
            environment.getProperty("harpocrates.init.vector")
        } returns initVector

        every {
            getSecretUseCase.getSecret(query)
        } returns secret

        val response = secretController.getSecretById(body)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.hasBody()).isTrue()

        response.body!!.run {
            assertThat(data.id).isEqualTo(secret.id)
            assertThat(data.value).isEqualTo(secret.value)
            assertThat(data.remainingViews).isEqualTo(secret.remainingViews)
        }

        verify {
            environment.getProperty("harpocrates.secret.key")
        }

        verify {
            environment.getProperty("harpocrates.init.vector")
        }

        verify {
            getSecretUseCase.getSecret(query)
        }
    }

    @Test
    fun `get secret by id when not found without password`() {
        val query = GetSecretQuery(
            id = secret.id,
            secretKey = secretKey,
            initVector = initVector,
            password = null
        )

        val body = GetSecretPayload(id = secret.id, password = null)

        every {
            environment.getProperty("harpocrates.secret.key")
        } returns secretKey

        every {
            environment.getProperty("harpocrates.init.vector")
        } returns initVector

        every {
            getSecretUseCase.getSecret(query)
        } returns null

        val response = secretController.getSecretById(body)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        verify {
            environment.getProperty("harpocrates.secret.key")
        }

        verify {
            environment.getProperty("harpocrates.init.vector")
        }

        verify {
            getSecretUseCase.getSecret(query)
        }
    }

    @Test
    fun `burn secret by id`() {
        val command = BurnSecretCommand(secret.id)

        val body = BurnSecretPayload(id = command.id)

        justRun {
            burnSecretUseCase.burnSecret(command)
        }

        val response = secretController.burnSecret(body)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        verify {
            burnSecretUseCase.burnSecret(command)
        }
    }
}