package dev.leachryan.harpocrates.web.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HarpocratesWebBackendApplication

fun main(args: Array<String>) {
	runApplication<HarpocratesWebBackendApplication>(*args)
}
