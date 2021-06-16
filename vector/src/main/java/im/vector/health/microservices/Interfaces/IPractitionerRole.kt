package im.vector.health.microservices.interfaces

import android.os.Parcelable

interface IPractitionerRole: Parcelable, MatrixItem {
    fun GetLongName():String
    fun GetShortName():String
    fun GetOrgName():String
    fun GetRoleName():String
    fun GetRoleCategory():String
    fun GetLocation():String
    fun GetPractitioners(callback: (List<IPractitioner>) -> Unit)
    fun GetID():String
    fun GetActive():Boolean
    override fun GetMatrixID(): String = "placeholder-room-id"
    override fun GetDisplayName(): String = GetLongName()
}