package dev.leachryan.harpocrates.web.backend.integration.factory

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object RedisTestContainerFactory {

    private const val EXPOSED_PORT = 6379

    fun makeRedisContainer(): GenericContainer<Nothing> {
        val redisContainer: GenericContainer<Nothing> = GenericContainer<Nothing>(
            DockerImageName.parse("eqalpha/keydb:latest")
        ).withExposedPorts(EXPOSED_PORT)

        return redisContainer
    }
}