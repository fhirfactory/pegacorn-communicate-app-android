package im.vector.microservices

data class FHIRIdentifier (
    @JvmField
    var type: String,
    @JvmField
    var use: String,
    @JvmField
    var value: String,
    @JvmField
    var leafValue: String
) {
    val fhirIdentifier: String
    get() {
        return "${type}|${use}|${value}"
    }
}