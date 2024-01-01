package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class DataTest {

    @Test
    fun `to data`() {
        val message = "message"

        val dataWrapped = message.toData()

        assertThat(dataWrapped.data).isEqualTo("message")
    }
}