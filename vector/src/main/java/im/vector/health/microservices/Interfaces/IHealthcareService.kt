package im.vector.health.microservices.interfaces

import android.os.Parcelable

interface IHealthcareService: Parcelable {
    fun GetLongName():String
    fun GetShortName():String
    fun GetID():String
    fun GetOrganisationUnit():String
}