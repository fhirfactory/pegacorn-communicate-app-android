package im.vector.microservices

data class FHIRDirectoryResponse<T>(
        @JvmField
        var id : String,
        @JvmField
        var created: Boolean,
        @JvmField
        var entry: T?
)