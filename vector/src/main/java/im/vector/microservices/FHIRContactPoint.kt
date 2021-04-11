package im.vector.microservices

data class FHIRContactPoint (
        @JvmField
        var name: String,
        @JvmField
        var value: String,
        @JvmField
        var rank: Int,
        @JvmField
        var type: String,
        @JvmField
        var use: String?
)