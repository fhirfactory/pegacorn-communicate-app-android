package im.vector.health.microservices.APIModel

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FHIRPractitionerRole(
        @JvmField
        var simplifiedID: String,
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
        var primaryRoleID: String,
        @JvmField
        var contactPoints: List<FHIRContactPoint>,
        @JvmField
        var primaryRoleCategoryID: String,
        @JvmField
        var roleHistory: FHIRRoleHistory?,
        @JvmField
        var activePractitionerSet: List<FHIRPractitioner>
) : Parcelable