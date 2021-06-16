package im.vector.health.microservices.interfaces

interface APIError {
    fun GetDescription(): String
    fun GetTitle(): String
}