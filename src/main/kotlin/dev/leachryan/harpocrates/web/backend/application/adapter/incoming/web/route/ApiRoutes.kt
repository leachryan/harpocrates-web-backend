package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.route

object ApiRoutes {

    object Secret {
        private const val commandRoot: String = "command"
        private const val queryRoot: String = "query"

        const val createSecretCommand: String = "$commandRoot/create-secret"
        
        const val burnSecretCommand: String = "$commandRoot/burn-secret"
        
        const val getSecretQuery: String = "$queryRoot/get-secret"
    }
}