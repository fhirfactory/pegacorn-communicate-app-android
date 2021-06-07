package im.vector.health.directory.people.model

import android.os.Parcelable
import im.vector.health.microservices.Interfaces.IPractitioner
import im.vector.health.microservices.Interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
class PractitionerItem (val practitioner: IPractitioner, var expanded: Boolean) : Parcelable, IPractitioner {
    override fun GetName(): String = practitioner.GetName()
    override fun GetID(): String = practitioner.GetID()
    override fun GetMatrixID(): String = practitioner.GetMatrixID()

    override fun GetRoles(callback: (List<IPractitionerRole>) -> Unit) = practitioner.GetRoles(callback)
}