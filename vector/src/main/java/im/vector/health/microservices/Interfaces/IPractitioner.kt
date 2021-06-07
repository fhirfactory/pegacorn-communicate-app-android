package im.vector.health.microservices.Interfaces

import android.os.Parcelable

interface IPractitioner: Parcelable {
    fun GetName():String
    fun GetRoles(callback: (List<IPractitionerRole>) -> Unit)
    fun GetID():String
    fun GetMatrixID(): String
}