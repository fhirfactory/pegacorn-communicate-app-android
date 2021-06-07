package im.vector.health.microservices.APIModel

object FHIRTools {
    fun getNameForIdentifier(idList: List<FHIRIdentifier>, type: String): String? {
        return idList.find { x -> x.type == type }?.value
    }
}