package im.vector.health.microservices.interfaces

import android.os.Parcelable

interface MatrixItem : Parcelable {
    fun GetMatrixID(): String
    fun GetDisplayName(): String = GetMatrixID()
    fun GetAvatarUrl(): String? = null
}