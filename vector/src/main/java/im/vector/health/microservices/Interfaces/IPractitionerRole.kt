package im.vector.health.microservices.Interfaces

import android.os.Parcelable

interface IPractitionerRole: Parcelable {
    fun GetLongName():String
    fun GetShortName():String
    fun GetOrgName():String
    fun GetRoleName():String
    fun GetRoleCategory():String
    fun GetLocation():String
    fun GetPractitioners(callback: (List<IPractitioner>) -> Unit)
    fun GetID():String
    fun GetActive():Boolean
}