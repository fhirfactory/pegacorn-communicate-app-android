package im.vector.health.microservices.APIModel

object FHIRTools {
    fun getNameForIdentifier(idList: List<FHIRIdentifier>, type: String): String? = idList.find { x -> x.type == type }?.value
}