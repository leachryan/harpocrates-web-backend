package dev.leachryan.harpocrates.web.backend.integration.infrastructure

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import dev.leachryan.harpocrates.web.backend.integration.configuration.TestRedisConfiguration
import dev.leachryan.harpocrates.web.backend.integration.setup.IntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.ContextConfiguration

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
@ContextConfiguration(classes = [TestRedisConfiguration::class])
class RedisIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `set a value to a key`() {
        assertThat(redisTemplate.opsForValue().set("test", "value")).isInstanceOf(Unit::class.java)
    }

    @Test
    fun `retrieve a value for a key`() {
        redisTemplate.opsForValue().set("test", "value")

        val retrievedValue = redisTemplate.opsForValue().get("test")

        assertThat(retrievedValue is String).isTrue()
        assertThat(retrievedValue).isEqualTo("value")
    }
}