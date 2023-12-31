package dev.leachryan.harpocrates.web.backend.integration.configuration

import dev.leachryan.harpocrates.web.backend.integration.factory.RedisTestContainerFactory
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class TestRedisConfiguration {

    init {
        val redisContainer = RedisTestContainerFactory.makeRedisContainer()
        redisContainer.start()
        System.setProperty("spring.data.redis.database", "0")
        System.setProperty("spring.data.redis.host", redisContainer.host)
        System.setProperty("spring.data.redis.port", redisContainer.firstMappedPort.toString())
    }
}