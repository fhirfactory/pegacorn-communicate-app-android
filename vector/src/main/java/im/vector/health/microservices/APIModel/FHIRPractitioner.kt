package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRPractitioner (
        @JvmField
        var simplifiedID: String,
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
        var currentPractitionerRoles: List<FHIRPractitionerRole>,
        @JvmField
        var matrixId: String,
        @JvmField
        var organisationStructure: List<FHIROrganizationItem>,
        @JvmField
        var mainJobTitle: String?,
        @JvmField
        var practitionerStatus: FHIRStatus
) : Parcelable