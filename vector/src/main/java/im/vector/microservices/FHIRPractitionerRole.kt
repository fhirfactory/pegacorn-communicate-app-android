package im.vector.microservices

import com.google.gson.annotations.SerializedName

data class FHIRPractitionerRole(
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var displayName: String,
        @JvmField
        var description: String,
        @JvmField
        var systemManaged: Boolean,
        @JvmField
        var primaryOrganizationID: String,
        @JvmField
        var primaryLocationID: String,
        @JvmField
        var contactPoints: List<FHIRContactPoint>,
        @JvmField
        var primaryRoleCategory: String,
        @JvmField
        var primaryRole: String
)