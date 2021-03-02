package io.marcofracassi

interface EventRepository {
    fun send(message: String)
}