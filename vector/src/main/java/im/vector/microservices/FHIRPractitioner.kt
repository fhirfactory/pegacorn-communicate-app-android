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
//        @JvmField
//        var currentPractitionerRoles: List<String>,
        @JvmField
        var practitionerRoleHistories: FHIRRoleHistory
) {
        val currentPractitionerRoles: List<String>
        get() {
                println("")
                return practitionerRoleHistories.roleHistories.filter { x -> x.endDate == null }.map { x -> x.identifier }
        }
        var roles: ArrayList<FHIRPractitionerRole> = ArrayList()
        var dataChangeEventListeners: ArrayList<()->Unit> = ArrayList()
        fun dataChanged(){
                dataChangeEventListeners.forEach { it() }
        }
}