package im.vector.health.directory.role.model

import android.os.Parcelable
import im.vector.health.microservices.Interfaces.IPractitioner
import im.vector.health.microservices.Interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
class PractitionerRoleItem(val practitionerRole: IPractitionerRole, var expanded: Boolean = false) : Parcelable, IPractitionerRole {
    override fun GetLongName(): String = practitionerRole.GetLongName()
    override fun GetShortName(): String = practitionerRole.GetShortName()
    override fun GetOrgName(): String = practitionerRole.GetOrgName()
    override fun GetRoleName(): String = practitionerRole.GetRoleName()
    override fun GetRoleCategory(): String = practitionerRole.GetRoleCategory()
    override fun GetLocation(): String = practitionerRole.GetLocation()
    override fun GetPractitioners(callback: (List<IPractitioner>) -> Unit) = practitionerRole.GetPractitioners(callback)
    override fun GetID(): String = practitionerRole.GetID()
    override fun GetActive(): Boolean = practitionerRole.GetActive()
}