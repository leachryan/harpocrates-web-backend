package dev.leachryan.harpocrates.web.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@SpringBootApplication
class HarpocratesWebBackendApplication

fun main(args: Array<String>) {
	runApplication<HarpocratesWebBackendApplication>(*args)
}
