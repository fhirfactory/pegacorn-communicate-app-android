package im.vector.health.directory.shared

import android.widget.Toast
import im.vector.health.microservices.interfaces.APIError

interface HandlesAPIErrors: IProvidesContext {
    fun displayError(err: APIError): Unit {
        Toast.makeText(getCurrentContext(),err.GetTitle(),Toast.LENGTH_SHORT).show()
    }
}