package dev.leachryan.harpocrates.web.backend.application.adapter.incoming.web.payload.response

data class Data<T>(val data: T)

inline fun <reified T> T.toData() = Data(this)
