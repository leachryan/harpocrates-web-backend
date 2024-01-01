package dev.leachryan.harpocrates.web.backend.integration

import dev.leachryan.harpocrates.web.backend.integration.setup.IntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
class HarpocratesWebBackendApplicationTests : IntegrationTest() {

	@Test
	fun contextLoads() {
	}

}
