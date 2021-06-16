package im.vector.health.microservices

import im.vector.health.microservices.interfaces.APIError

class BasicAPIError(val title: String): APIError {
    override fun GetDescription(): String = title
    override fun GetTitle(): String = title
}