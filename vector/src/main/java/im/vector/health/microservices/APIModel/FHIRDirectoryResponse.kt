package im.vector.health.microservices.APIModel

data class FHIRDirectoryResponse<T>(
        @JvmField
        var id : String,
        @JvmField
        var created: Boolean,
        @JvmField
        var entry: T?
)