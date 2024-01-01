package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.route

object ApiRoutes {

    object Secret {
        const val root: String = "/secret"

        const val secretById: String = "$root/{id}"
    }
}