package dev.leachryan.harpocrates.web.backend.integration.application.adapter.incoming.web.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.route.ApiRoutes
import dev.leachryan.harpocrates.web.backend.integration.configuration.TestRedisConfiguration
import dev.leachryan.harpocrates.web.backend.integration.setup.IntegrationTest
import io.restassured.RestAssured
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import java.util.UUID


@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@ContextConfiguration(classes = [TestRedisConfiguration::class])
class SecretControllerIntegrationTest : IntegrationTest() {

    @LocalServerPort
    private lateinit var port: String

    @Test
    fun `create secret without password`() {
        val request = RestAssured.given()

        val createSecretPayload = JSONObject()
        createSecretPayload.put("value", "secret")
        createSecretPayload.put("maxViews", 10)

        request.contentType("application/json")
        request.body(createSecretPayload.toString())
        request.port(port.toInt())

        val response = request.post("/api/${ApiRoutes.Secret.createSecretCommand}")

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.headers.find { it.name == "Location" }).isNotNull()
    }

    @Test
    fun `get secret by id when found without password`() {
        val createSecretRequest = RestAssured.given()

        val createRequestPayload = JSONObject()
        createRequestPayload.put("value", "secret")
        createRequestPayload.put("maxViews", 10)

        createSecretRequest.contentType("application/json")
        createSecretRequest.body(createRequestPayload.toString())
        createSecretRequest.port(port.toInt())

        val createSecretResponse = createSecretRequest.post("/api/${ApiRoutes.Secret.createSecretCommand}")
        val secretId = createSecretResponse.headers.find { it.name == "Location" }!!.value

        val getSecretRequest = RestAssured.given()

        val getSecretRequestPayload = JSONObject()
        getSecretRequestPayload.put("id", secretId)
        getSecretRequestPayload.put("password", null)

        getSecretRequest.contentType("application/json")
        getSecretRequest.port(port.toInt())
        getSecretRequest.body(getSecretRequestPayload.toString())

        val getSecretResponse = getSecretRequest.post("/api/${ApiRoutes.Secret.getSecretQuery}")

        assertThat(getSecretResponse.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(getSecretResponse.jsonPath().getString("data.id")).isEqualTo(secretId)
        assertThat(getSecretResponse.jsonPath().getString("data.value")).isEqualTo("secret")
        assertThat(getSecretResponse.jsonPath().getInt("data.remainingViews")).isEqualTo(9)
    }

    @Test
    fun `get secret by id when not found without password`() {
        val secretId = UUID.randomUUID()

        val getSecretRequest = RestAssured.given()

        val getSecretPayload = JSONObject()
        getSecretPayload.put("password", null)
        getSecretPayload.put("id", secretId)

        getSecretRequest.contentType("application/json")
        getSecretRequest.port(port.toInt())
        getSecretRequest.body(getSecretPayload.toString())

        val getSecretResponse = getSecretRequest.post("/api/${ApiRoutes.Secret.getSecretQuery}")

        assertThat(getSecretResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `burn secret by id`() {
        val createSecretRequest = RestAssured.given()

        val createSecretPayload = JSONObject()
        createSecretPayload.put("value", "secret")
        createSecretPayload.put("maxViews", 10)

        createSecretRequest.contentType("application/json")
        createSecretRequest.body(createSecretPayload.toString())
        createSecretRequest.port(port.toInt())

        val createSecretResponse = createSecretRequest.post("/api/${ApiRoutes.Secret.createSecretCommand}")
        val secretId = createSecretResponse.headers.find { it.name == "Location" }!!.value

        val burnSecretRequest = RestAssured.given()

        val burnSecretPayload = JSONObject()
        burnSecretPayload.put("id", secretId)

        burnSecretRequest.contentType("application/json")
        burnSecretRequest.port(port.toInt())
        burnSecretRequest.body(burnSecretPayload.toString())

        val burnSecretResponse = burnSecretRequest.post("/api/${ApiRoutes.Secret.burnSecretCommand}")

        assertThat(burnSecretResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}