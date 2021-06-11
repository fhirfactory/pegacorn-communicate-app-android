package im.vector.health.microservices.mock

import im.vector.health.microservices.interfaces.IPatient
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class MockPatient(val Name: String, val URN: String, val DOB: Date): IPatient {
    override fun GetName(): String = Name

    override fun GetDOB(): Date = DOB

    override fun GetURN(): String = URN

}