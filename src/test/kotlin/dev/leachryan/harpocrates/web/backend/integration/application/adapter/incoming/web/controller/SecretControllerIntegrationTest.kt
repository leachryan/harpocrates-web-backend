package dev.leachryan.harpocrates.web.backend.integration.application.adapter.incoming.web.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
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
import java.util.*


@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@ContextConfiguration(classes = [TestRedisConfiguration::class])
class SecretControllerIntegrationTest : IntegrationTest() {

    @LocalServerPort
    private lateinit var port: String

    @Test
    fun `create secret`() {
        val request = RestAssured.given()

        val payload = JSONObject()
        payload.put("value", "secret")
        payload.put("maxViews", 10)

        request.contentType("application/json")
        request.body(payload.toString())
        request.port(port.toInt())

        val response = request.post("/api/secret")

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.headers.find { it.name == "Location" }).isNotNull()
    }

    @Test
    fun `get secret by id when found`() {
        val createSecretRequest = RestAssured.given()

        val createRequestPayload = JSONObject()
        createRequestPayload.put("value", "secret")
        createRequestPayload.put("maxViews", 10)

        createSecretRequest.contentType("application/json")
        createSecretRequest.body(createRequestPayload.toString())
        createSecretRequest.port(port.toInt())

        val createSecretResponse = createSecretRequest.post("/api/secret")
        val secretId = createSecretResponse.headers.find { it.name == "Location" }!!.value

        val getSecretRequest = RestAssured.given()

        getSecretRequest.contentType("application/json")
        getSecretRequest.port(port.toInt())

        val getSecretResponse = getSecretRequest.get("/api/secret/$secretId")

        assertThat(getSecretResponse.statusCode).isEqualTo(HttpStatus.OK.value())
        assertThat(getSecretResponse.jsonPath().getString("data.id")).isEqualTo(secretId)
        assertThat(getSecretResponse.jsonPath().getString("data.value")).isEqualTo("secret")
        assertThat(getSecretResponse.jsonPath().getInt("data.remainingViews")).isEqualTo(9)
    }

    @Test
    fun `get secret by id when not found`() {
        val getSecretRequest = RestAssured.given()

        getSecretRequest.contentType("application/json")
        getSecretRequest.port(port.toInt())

        val secretId = UUID.randomUUID()

        val getSecretResponse = getSecretRequest.get("/api/secret/$secretId")

        assertThat(getSecretResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun `burn secret by id`() {
        val createSecretRequest = RestAssured.given()

        val createRequestPayload = JSONObject()
        createRequestPayload.put("value", "secret")
        createRequestPayload.put("maxViews", 10)

        createSecretRequest.contentType("application/json")
        createSecretRequest.body(createRequestPayload.toString())
        createSecretRequest.port(port.toInt())

        val createSecretResponse = createSecretRequest.post("/api/secret")
        val secretId = createSecretResponse.headers.find { it.name == "Location" }!!.value

        val burnSecretRequest = RestAssured.given()

        burnSecretRequest.contentType("application/json")
        burnSecretRequest.port(port.toInt())

        val burnSecretResponse = burnSecretRequest.delete("/api/secret/$secretId")

        assertThat(burnSecretResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT.value())
    }
}