package im.vector.microservices

import com.google.gson.annotations.SerializedName

data class FHIRPractitionerRole(
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var displayName: String,
        @JvmField
        var systemManaged: Boolean,
        @JvmField
        var primaryOrganizationID: FHIRIdentifier,
        @JvmField
        var primaryLocationID: FHIRIdentifier,
        @JvmField
        var contactPoints: List<FHIRContactPoint>,
        @JvmField
        var primaryRoleCategory: String,
        @JvmField
        var primaryRole: String
)