package im.vector.microservices

data class FHIRPractitioner (
        @JvmField
        var identifiers: List<FHIRIdentifier>
)