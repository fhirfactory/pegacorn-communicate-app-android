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
        var contactPoints: List<FHIRContactPoint>,
        @JvmField
        var currentPractitionerRoles: List<String>
) {
        var roles: ArrayList<FHIRPractitionerRole> = ArrayList()
        var dataChangeEventListeners: ArrayList<()->Unit> = ArrayList()
        fun dataChanged(){
                dataChangeEventListeners.forEach { it() }
        }
}