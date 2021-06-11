package im.vector.health.directory.people.model

import android.os.Parcelable
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.health.microservices.interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
class PractitionerItem (val practitioner: IPractitioner, var expanded: Boolean) : Parcelable, IPractitioner {
    override fun GetName(): String = practitioner.GetName()
    override fun GetID(): String = practitioner.GetID()
    override fun GetMatrixID(): String = practitioner.GetMatrixID()
    override fun GetJobTitle(): String = practitioner.GetJobTitle()
    override fun GetBusinessUnit(): String = practitioner.GetBusinessUnit()
    override fun GetOrganization(): String = practitioner.GetOrganization()
    override fun GetRoles(callback: (List<IPractitionerRole>) -> Unit) = practitioner.GetRoles(callback)
    override fun GetEmailAddress(): String? = practitioner.GetEmailAddress()
    override fun GetPhoneNumber(): String? = practitioner.GetPhoneNumber()
}