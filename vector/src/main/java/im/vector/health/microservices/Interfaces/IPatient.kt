package im.vector.health.microservices.interfaces

import android.os.Parcelable
import java.util.*

interface IPatient: Parcelable {
    fun GetName():String
    fun GetDOB():Date
    fun GetURN():String
}