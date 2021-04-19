package im.vector.microservices

data class FHIRPractitioner (
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var displayName: String,
        @JvmField
        var systemManaged: Boolean,
        @JvmField
        var officialName: FHIRName,
        @JvmField
        var otherNames: List<FHIRName>,
        @JvmField
        var contactPoints: List<FHIRContactPoint>
)