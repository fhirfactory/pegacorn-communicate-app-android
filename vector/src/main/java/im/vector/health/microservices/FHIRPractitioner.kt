package im.vector.health.microservices

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
        var currentPractitionerRoles: List<String>,
        @JvmField
        var practitionerRoleHistories: FHIRRoleHistory?
) {
        val assignedPractitionerRoles: List<String>
        get() {
                if (currentPractitionerRoles.count() > 0) return currentPractitionerRoles
                practitionerRoleHistories?.let{ return it.roleHistories.filter { x -> x.endDate == null }.map { x -> x.identifier } }
                return ArrayList()
        }
        var roles: ArrayList<FHIRPractitionerRole> = ArrayList()
        var dataChangeEventListeners: ArrayList<()->Unit> = ArrayList()
        fun dataChanged(){
                dataChangeEventListeners.forEach { it() }
        }
}