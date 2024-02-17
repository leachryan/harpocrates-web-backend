package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.controller

import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.BurnSecretPayload
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.CreateSecretPayload
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.request.GetSecretPayload
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response.Data
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response.SecretResponse
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response.toData
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.route.ApiRoutes
import dev.leachryan.harpocrates.web.backend.core.domain.command.BurnSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.command.CreateSecretCommand
import dev.leachryan.harpocrates.web.backend.core.domain.query.GetSecretQuery
import dev.leachryan.harpocrates.web.backend.core.port.incoming.BurnSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.incoming.CreateSecretUseCase
import dev.leachryan.harpocrates.web.backend.core.port.incoming.GetSecretUseCase
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@CrossOrigin(origins = ["*"])
class SecretController(
    private val createSecretUseCase: CreateSecretUseCase,
    private val getSecretUseCase: GetSecretUseCase,
    private val burnSecretUseCase: BurnSecretUseCase,
    private val environment: Environment
) {

    @PostMapping(ApiRoutes.Secret.createSecretCommand)
    fun createSecret(
        @RequestBody
        request: CreateSecretPayload
    ): ResponseEntity<String> {
        val command = CreateSecretCommand(
            value = request.value,
            maxViews = request.maxViews,
            secretKey = environment.getProperty("harpocrates.secret.key").toString(),
            initVector = environment.getProperty("harpocrates.init.vector").toString(),
            password = request.password
        )

        val secret = createSecretUseCase.createSecret(command)

        return ResponseEntity.created(URI.create("${secret.id}")).build()
    }

    @PostMapping(ApiRoutes.Secret.getSecretQuery)
    fun getSecretById(
        @RequestBody body: GetSecretPayload,
    ): ResponseEntity<Data<SecretResponse>> {
        val query = GetSecretQuery(
            id = body.id,
            secretKey = environment.getProperty("harpocrates.secret.key").toString(),
            initVector = environment.getProperty("harpocrates.init.vector").toString(),
            password = body.password
        )

        val secret = getSecretUseCase.getSecret(query)

        return secret?.let {
            val response = SecretResponse(
                id = it.id,
                value = it.value,
                remainingViews = it.remainingViews
            )

            ResponseEntity.ok(response.toData())
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping(ApiRoutes.Secret.burnSecretCommand)
    fun burnSecret(
        @RequestBody body: BurnSecretPayload
    ): ResponseEntity<String> {
        val command = BurnSecretCommand(body.id)

        burnSecretUseCase.burnSecret(command)

        return ResponseEntity.noContent().build()
    }
}