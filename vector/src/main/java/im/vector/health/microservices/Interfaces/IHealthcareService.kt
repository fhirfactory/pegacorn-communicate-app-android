package im.vector.health.microservices.Interfaces

import android.os.Parcelable

interface IHealthcareService: Parcelable {
    fun GetLongName():String
    fun GetShortName():String
    fun GetID():String
    fun GetDescription():String
}