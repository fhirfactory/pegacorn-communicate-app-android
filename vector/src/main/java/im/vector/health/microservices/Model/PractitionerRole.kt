package im.vector.health.microservices.model

import android.os.Parcelable
import im.vector.health.microservices.APIModel.FHIRPractitionerRole
import im.vector.health.microservices.APIModel.FHIRTools
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.health.microservices.interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
open class PractitionerRole(var practitionerRole: FHIRPractitionerRole) : IPractitionerRole, Parcelable {
    override fun GetLongName(): String = FHIRTools.getNameForIdentifier(practitionerRole.identifiers, "LongName")?:practitionerRole.displayName
    override fun GetShortName(): String = practitionerRole.displayName
    override fun GetOrgName(): String = practitionerRole.primaryOrganizationID
    override fun GetRoleName(): String = practitionerRole.primaryRoleID
    override fun GetRoleCategory(): String = practitionerRole.primaryRoleCategoryID
    override fun GetLocation(): String = practitionerRole.primaryLocationID
    override fun GetPractitioners(callback: (List<IPractitioner>) -> Unit) = callback(practitionerRole.activePractitionerSet.map { Practitioner(it) })
    override fun GetID(): String = practitionerRole.simplifiedID
    override fun GetActive(): Boolean = practitionerRole.activePractitionerSet.any()
}