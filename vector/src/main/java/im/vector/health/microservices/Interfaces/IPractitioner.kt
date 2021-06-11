package im.vector.health.microservices.interfaces

import android.os.Parcelable

interface IPractitioner: Parcelable {
    fun GetName():String
    fun GetRoles(callback: (List<IPractitionerRole>) -> Unit)
    fun GetJobTitle(): String
    fun GetBusinessUnit(): String
    fun GetOrganization(): String
    fun GetID():String
    fun GetMatrixID(): String
    fun GetPhoneNumber(): String?
    fun GetEmailAddress(): String?
}