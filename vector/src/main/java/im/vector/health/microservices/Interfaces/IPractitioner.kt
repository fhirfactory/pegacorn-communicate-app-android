package im.vector.health.microservices.interfaces

import android.os.Parcelable

interface IPractitioner: Parcelable, MatrixItem {
    fun GetName():String
    fun GetRoles(callback: (List<IPractitionerRole>) -> Unit)
    fun GetJobTitle(): String
    fun GetBusinessUnit(): String
    fun GetOrganization(): String
    fun GetID():String
    fun GetPhoneNumber(): String?
    fun GetEmailAddress(): String?
    fun GetOnlineStatus(): Boolean
    fun GetActiveStatus(): Boolean = GetOnlineStatus()
    override fun GetDisplayName(): String = GetName()
}